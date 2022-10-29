package com.crptm.lambdaservice.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CollectionUtil {

    public static List<String> getListFromString(String charSequence) {
        if (charSequence == null) {
            return Collections.emptyList();
        }
        charSequence = charSequence.replaceAll(" ", "");
        return Arrays.asList(charSequence.split(","));
    }
}
