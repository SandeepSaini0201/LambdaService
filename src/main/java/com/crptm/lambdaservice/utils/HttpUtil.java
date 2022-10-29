package com.crptm.lambdaservice.utils;

import com.crptm.lambdaservice.constants.ErrorConstants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public final class HttpUtil {

    private static final String HTTP_URI = "http";
    private static final String DEFAULT_HTTP_RES_HANDLER = "application/json";

    public static String prepareURL(String URI, final Map<String, String> queryParams) {
        if (StringUtils.isEmpty(URI) || !URI.startsWith(HTTP_URI)) {
            throw new InvalidParameterException(ErrorConstants.INVALID_URI);
        }
        if (queryParams == null || queryParams.size() == 0) {
            return URI;
        }
        final StringBuilder builder = new StringBuilder(URI.trim());
        builder.append("?");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            builder.append(entry.getKey().trim()).append("=").append(entry.getValue().trim()).append("&");
        }
        return builder.substring(0, builder.length());
    }

    public static String[] getHeaders(final Map<String, String> headers) {
        int size = headers == null ? 0 : headers.size();
        final String[] strHeaders = new String[(size + 2) * 2];
        int i = 0;
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                strHeaders[i++] = entry.getKey();
                strHeaders[i++] = entry.getValue();
            }
        }
        strHeaders[i++] = "Accept";
        strHeaders[i++] = DEFAULT_HTTP_RES_HANDLER;
        strHeaders[i++] = "Content-Type";
        strHeaders[i] = DEFAULT_HTTP_RES_HANDLER;
        return strHeaders;
    }

    public static String prepareCurrency(final List<String> currencies) {
        final StringBuilder builder = new StringBuilder();
        for (String entry : currencies) {
            builder.append(entry).append(",");
        }
        return builder.substring(0, builder.length());
    }

    public static BigDecimal getBigDecimalValueToFourPlacesDecimal(final BigDecimal value) {
        return value.setScale(4, RoundingMode.DOWN);
    }

    public static String getBigDecimalValueToFourDecimalPlaces(final BigDecimal value) {
        return value.setScale(4, RoundingMode.DOWN).toString();
    }
}