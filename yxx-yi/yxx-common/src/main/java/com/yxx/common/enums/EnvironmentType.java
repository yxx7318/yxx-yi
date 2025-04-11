package com.yxx.common.enums;

public enum EnvironmentType {

    DEV("dev"),

    TEST("test"),

    PROD("prod");

    private final String type;

    EnvironmentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
