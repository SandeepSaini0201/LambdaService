package com.crptm.lambdaservice.http;

import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.exception.HttpRequestException;
import com.crptm.lambdaservice.http.res.HttpBaseResponse;
import com.crptm.lambdaservice.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * This supports Http sync calls for http version 1.1
 * <p>
 * If needed another HTTP version or Async calls then create another class
 * </p>
 * <p>
 * By default connection pool which java.net.HTTP uses has size = 0 which means unlimited.
 * <p>
 * If we need to set the limit then we can set the limit with environment variable
 * -Djdk.httpclient.connectionPoolSize but this is used only for HTTP1.1 and not for HTTP2
 * </p>
 * <p>
 * By default keep alive timeout is 1200 seconds(20 minutes) and if we want to change the keep alive
 * timeout then set this environment variable -Djdk.httpclient.keepalive.timeout
 * </p>
 */

@Component
@Slf4j
public class HttpSyncClient {
    private static final String DEFAULT_BODY = "{}";

    private HttpClient client;
    private ObjectMapper mapper;

    private Integer clientTimeout = 100;

    public <T extends HttpBaseResponse> T post(Class<T> type, String URL, final Map<String, String> headers, final Map<String, String> queryParams
            , Object body) {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HttpUtil.prepareURL(URL, queryParams)))
                    .headers(HttpUtil.getHeaders(headers))
                    .POST(getBodyPublisher(body))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            final String res = getBody(response);
            T resObj = this.mapper.readValue(res, type);
            resObj.setCode(response.statusCode());
            return resObj;
        } catch (IOException | InterruptedException e) {
            log.error("Exception occurred during HTTP POST request ", e);
            throw new HttpRequestException(String.format(ErrorConstants.HTTP_REQ_FAILURE, "POST", e.getMessage()), e);
        }
    }

    @PostConstruct
    private void initializeClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(this.clientTimeout))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private HttpRequest.BodyPublisher getBodyPublisher(final Object body) throws JsonProcessingException {
        if (Objects.isNull(body)) return HttpRequest.BodyPublishers.noBody();
        return HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(body));
    }

    private String getBody(final HttpResponse<String> response) {
        final String body = response.body();
        if (StringUtils.isBlank(body)) {
            return DEFAULT_BODY;
        }
        return body;
    }
}
