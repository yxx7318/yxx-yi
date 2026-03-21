package com.yxx.ai.enums;


import org.springframework.ai.chat.messages.MessageType;

public enum MessageTypeEnum {

    USER(1),
    ASSISTANT(2),
    SYSTEM(3),
    TOOL(4);

    private final Integer value;

    private MessageTypeEnum(Integer value) {
        this.value = value;
    }

    public static MessageTypeEnum fromValue(Integer value) {
        for (MessageTypeEnum messageType : values()) {
            if (messageType.getValue().equals(value)) {
                return messageType;
            }
        }

        throw new IllegalArgumentException("Invalid MessageType value: " + value);
    }

    public static int fromValue(MessageType value) {
        switch (value) {
            case USER:
                return 1;
            case ASSISTANT:
                return 2;
            case SYSTEM:
                return 3;
            case TOOL:
                return 4;
        }
        throw new IllegalArgumentException("Invalid MessageType value: " + value);
    }


    public Integer getValue() {
        return this.value;
    }
}
