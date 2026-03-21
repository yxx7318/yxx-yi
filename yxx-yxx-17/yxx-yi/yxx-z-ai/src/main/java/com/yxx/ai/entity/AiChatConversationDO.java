package com.yxx.ai.entity;

import java.io.Serial;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 会话DO对象 ai_chat_conversation
 *
 * @author yxx
 * @date 2026-01-21
 */
@Schema(description = "会话DO实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "ai_chat_conversation", autoResultMap = true)
public class AiChatConversationDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话id")
    @TableId(value = "chat_conversation_id", type = IdType.AUTO)
    private Long chatConversationId;

    @Schema(description = "会话分组")
    @TableField("chat_group")
    @Excel(name = "会话分组")
    private String chatGroup;

    @Schema(description = "会话标题")
    @TableField("chat_title")
    @Excel(name = "会话标题")
    private String chatTitle;

}
