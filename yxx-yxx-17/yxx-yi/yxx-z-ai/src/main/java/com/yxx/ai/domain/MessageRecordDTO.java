package com.yxx.ai.domain;


import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;
import java.util.Map;

public class MessageRecordDTO {

    private MessageType messageType;

    private String text;

    private Map<String, Object> metadata;

    private List<AssistantMessage.ToolCall> toolCalls;

    public MessageRecordDTO(Message message) {
        this.messageType = message.getMessageType();
        this.text = message.getText();
        this.metadata = message.getMetadata();
        if (message instanceof AssistantMessage am) {
            this.toolCalls = am.getToolCalls();
        }
    }

    public Message toMessage() {
        return switch (messageType) {
            case SYSTEM -> new SystemMessage(text);
            case USER -> new UserMessage(text);
            case ASSISTANT -> new AssistantMessage(text, metadata, toolCalls, List.of());
            default -> throw new IllegalArgumentException("Unsupported message type: " + messageType);
        };
    }

    public MessageRecordDTO() {
    }

    public MessageRecordDTO(MessageType messageType, String text, Map<String, Object> metadata, List<AssistantMessage.ToolCall> toolCalls) {
        this.messageType = messageType;
        this.text = text;
        this.metadata = metadata;
        this.toolCalls = toolCalls;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<AssistantMessage.ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<AssistantMessage.ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
}
