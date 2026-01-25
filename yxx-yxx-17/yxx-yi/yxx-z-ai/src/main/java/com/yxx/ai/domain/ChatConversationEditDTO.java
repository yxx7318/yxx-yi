package com.yxx.ai.domain;

import io.swagger.v3.oas.annotations.media.Schema;


public class ChatConversationEditDTO {

    @Schema(description = "会话id")
    private Long chatConversationId;

    @Schema(description = "会话分组")
    private String chatGroup;

    @Schema(description = "会话标题")
    private String chatTitle;

    public ChatConversationEditDTO() {
    }

    public ChatConversationEditDTO(Long chatConversationId, String chatGroup, String chatTitle) {
        this.chatConversationId = chatConversationId;
        this.chatGroup = chatGroup;
        this.chatTitle = chatTitle;
    }

    public Long getChatConversationId() {
        return chatConversationId;
    }

    public void setChatConversationId(Long chatConversationId) {
        this.chatConversationId = chatConversationId;
    }

    public String getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(String chatGroup) {
        this.chatGroup = chatGroup;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }
}
