package com.yxx.ai.domain;

import com.yxx.common.core.domain.dto.FileUploadDTO;
import com.yxx.common.core.utils.JacksonUtils;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

import static com.yxx.ai.constant.Constants.RESOURCE_MEDIA_DATA;


public class MessageVO {

    private String role;

    private String content;

    private List<FileUploadDTO> files;

    public MessageVO(Message message) {
        switch (message.getMessageType()) {
            case USER:
                role = "user";
                break;
            case ASSISTANT:
                role = "assistant";
                break;
            default:
                role = "";
                break;
        }
        this.content = message.getText();
//        this.files = JacksonUtils.parseArray(message.getMetadata().get(RESOURCE_MEDIA_DATA).toString(), FileUploadDTO.class);
    }

    public MessageVO() {
    }

    public MessageVO(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
