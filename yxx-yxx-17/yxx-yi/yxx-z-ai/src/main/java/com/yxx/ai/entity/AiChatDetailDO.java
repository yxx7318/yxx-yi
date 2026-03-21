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
 * 会话详细DO对象 ai_chat_detail
 *
 * @author yxx
 * @date 2026-01-21
 */
@Schema(description = "会话详细DO实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "ai_chat_detail", autoResultMap = true)
public class AiChatDetailDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话详细id")
    @TableId(value = "chat_detail_id", type = IdType.AUTO)
    private Long chatDetailId;

    @Schema(description = "会话id")
    @TableField("chat_conversation_id")
    @Excel(name = "会话id")
    private Long chatConversationId;

    @Schema(description = "消息类型（1代表user 2代表assistant 3代表system 4代表tool）")
    @TableField("message_type")
    @Excel(name = "消息类型", readConverterExp = "1=代表user,2=代表assistant,3=代表system,4=代表tool")
    private Integer messageType;

    @Schema(description = "内容")
    @TableField("content")
    @Excel(name = "内容")
    private String content;

    @Schema(description = "附件")
    @TableField("attachment")
    @Excel(name = "附件")
    private String attachment;

}
