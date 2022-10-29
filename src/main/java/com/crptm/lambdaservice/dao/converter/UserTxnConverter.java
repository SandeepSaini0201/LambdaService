package com.crptm.lambdaservice.dao.converter;

import com.crptm.lambdaservice.dao.entity.RefundTxnEntity;
import com.crptm.lambdaservice.dao.entity.UserTxnEntity;
import com.crptm.lambdaservice.pojo.RefundTxnPOJO;
import com.crptm.lambdaservice.pojo.UserTxnPOJO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserTxnConverter {
    public List<UserTxnPOJO> userTxnEntitiesToPOJOs(List<UserTxnEntity> userTxnEntities) {
        return Optional.ofNullable(userTxnEntities).orElseGet(Collections::emptyList)
                .stream()
                .map(userTxnEntity -> this.getUserTxnPOJOFromUserTxnEntity(userTxnEntity).get())
                .collect(Collectors.toList());
    }

    public Optional<UserTxnPOJO> getUserTxnPOJOFromUserTxnEntity(final UserTxnEntity userTxnEntity) {
        if (Objects.isNull(userTxnEntity)) return Optional.empty();
        return Optional.of(UserTxnPOJO.builder()
                .id(userTxnEntity.getId())
                .merchantId(userTxnEntity.getMerchantId())
                .userId(userTxnEntity.getUserId())
                .paymentIntentId(userTxnEntity.getPaymentIntentId())
                .vendorPaymentIntentId(userTxnEntity.getVendorPaymentIntentId())
                .amount(userTxnEntity.getAmount())
                .currency(userTxnEntity.getCurrency())
                .status(userTxnEntity.getStatus())
                .txnType(userTxnEntity.getTxnType())
                .paymentVendor(userTxnEntity.getPaymentVendor())
                .paymentMethod(userTxnEntity.getPaymentMethod())
                .futureUsage(userTxnEntity.getFutureUsage())
                .returnURL(userTxnEntity.getReturnURL())
                .metadata(userTxnEntity.getMetadata())
                .txnDate(userTxnEntity.getTxnDate())
                .createdAt(userTxnEntity.getCreatedAt())
                .updatedAt(userTxnEntity.getUpdatedAt())
                .version(userTxnEntity.getVersion())
                .build());
    }
}
