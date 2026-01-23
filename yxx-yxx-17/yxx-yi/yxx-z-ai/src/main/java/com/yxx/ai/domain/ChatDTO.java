package com.yxx.ai.domain;

import com.yxx.common.core.domain.dto.FileUploadDTO;

import java.util.List;

public class ChatDTO {

    private String prompt;

    private String conversationId;

    private List<FileUploadDTO> files;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<FileUploadDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }
}
