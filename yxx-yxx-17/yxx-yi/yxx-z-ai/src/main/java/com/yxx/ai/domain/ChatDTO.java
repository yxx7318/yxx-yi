package com.yxx.ai.domain;

import com.yxx.common.core.domain.dto.FileUploadDTO;

import java.util.List;

public class ChatDTO {

    private String prompt;
    private String chatId;
    private List<FileUploadDTO> files;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<FileUploadDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }
}
