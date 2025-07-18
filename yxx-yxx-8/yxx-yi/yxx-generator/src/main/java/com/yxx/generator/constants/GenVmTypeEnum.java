package com.yxx.generator.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GenVmTypeEnum {

    YXX("yxx"),
    RUO_YI("ruoyi");

    private final String vmType;

    GenVmTypeEnum(String vmType) {
        this.vmType = vmType;
    }

    public String getValue() {
        return vmType;
    }

    public static final Map<String, GenVmTypeEnum> MAP = Arrays.stream(GenVmTypeEnum.values())
            .collect(Collectors.toMap(GenVmTypeEnum::getValue, entry -> entry));
}
