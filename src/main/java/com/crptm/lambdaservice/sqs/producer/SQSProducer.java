package com.crptm.lambdaservice.sqs.producer;

import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.enums.EnumSQSQueue;
import com.crptm.lambdaservice.exception.SQSMessagingException;
import com.crptm.lambdaservice.sqs.store.SQSQueueDetail;
import com.crptm.lambdaservice.utils.MapperUtil;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SQSProducer {

    @Autowired
    private QueueMessagingTemplate template;

    @Autowired
    private SQSQueueDetail sqsQueueDetails;

    public void produceMessage(final EnumSQSQueue queueName, final Object message) {
        try {
            this.template.send(
                    this.sqsQueueDetails.getURLByQueueName(queueName),
                    MessageBuilder.withPayload(MapperUtil.writeValuesAsString(message)).build()
            );
        } catch (MessagingException e) {
            String errorMsg = String.format(ErrorConstants.SQS_MESSAGING_ERROR, queueName.name(), e.getMessage());
            log.error(errorMsg, e);
            throw new SQSMessagingException(errorMsg);
        }
    }
}
