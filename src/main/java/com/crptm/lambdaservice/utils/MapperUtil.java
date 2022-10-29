package com.crptm.lambdaservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public final class MapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String writeValuesAsString(final Object object) {
        try {
            if (Objects.isNull(object)) {
                return "{}";
            }
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException("");
        }
    }
}
