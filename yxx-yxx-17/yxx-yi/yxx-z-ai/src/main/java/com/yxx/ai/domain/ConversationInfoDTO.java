package com.yxx.ai.domain;

import com.yxx.common.core.domain.dto.FileUploadDTO;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.common.utils.StringUtils;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

import static com.yxx.ai.constant.Constants.CONVERSATION_INFO_DATA;

public class ConversationInfoDTO {

    private LoginUser loginUser;

    private String chatDetailId;

    private String conversationId;

    private List<FileUploadDTO> files;

    public ConversationInfoDTO() {
    }

    public ConversationInfoDTO(String conversationId, List<FileUploadDTO> files) {
        this.conversationId = conversationId;
        this.files = files;
    }

    public ConversationInfoDTO(LoginUser loginUser, String chatDetailId, String conversationId, List<FileUploadDTO> files) {
        this.loginUser = loginUser;
        this.chatDetailId = chatDetailId;
        this.conversationId = conversationId;
        this.files = files;
    }

    public static String convertString(ConversationInfoDTO conversationInfoDTO) {
        return JacksonUtils.toJsonString(conversationInfoDTO);
    }

    public static ConversationInfoDTO convertBean(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return JacksonUtils.parseObject(text, ConversationInfoDTO.class);
    }

    public static ConversationInfoDTO convertBean(Object o) {
        if (StringUtils.isNull(o) || ! (o instanceof ConversationInfoDTO)) {
            return null;
        }
        return (ConversationInfoDTO) o;
    }

    public static List<FileUploadDTO> convertFiles(Message message) {
        if (StringUtils.isNull(message) || StringUtils.isEmpty(message.getMetadata())) {
            return null;
        }
        Object conversationInfoDTO = message.getMetadata().get(CONVERSATION_INFO_DATA);
        if (conversationInfoDTO instanceof ConversationInfoDTO) {
            return ((ConversationInfoDTO) conversationInfoDTO).getFiles();
        }
        return null;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public String getChatDetailId() {
        return chatDetailId;
    }

    public void setChatDetailId(String chatDetailId) {
        this.chatDetailId = chatDetailId;
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
