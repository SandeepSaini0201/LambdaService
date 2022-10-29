package com.crptm.lambdaservice.enums;

import lombok.Getter;

@Getter
public enum EnumEnvironment {
    DEVELOPMENT("dev"),
    STAGING("stg"),
    PRODUCTION("prod");

    private final String environmentName;

    EnumEnvironment(String environmentName) {
        this.environmentName = environmentName;
    }
}
