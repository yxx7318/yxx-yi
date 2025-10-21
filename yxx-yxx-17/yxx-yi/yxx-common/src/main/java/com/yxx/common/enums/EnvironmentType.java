package com.yxx.common.enums;

public enum EnvironmentType {

    DEV("dev", "/dev-api"),

    TEST("test", "/test-api"),

    PROD("prod", "/prod-api");

    private final String type;

    private final String apiPrefix;

    EnvironmentType(String type, String apiPrefix) {
        this.type = type;
        this.apiPrefix = apiPrefix;
    }

    public String getType() {
        return type;
    }

    public String getApiPrefix() {
        return apiPrefix;
    }
}
