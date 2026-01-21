package com.yxx.ai.entity;

import java.io.Serial;
import com.yxx.common.core.domain.BaseEditDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import jakarta.validation.constraints.NotNull;

/**
 * 会话详细EditDTO对象 ai_chat_detail
 *
 * @author yxx
 * @date 2026-01-21
 */
@Schema(description = "会话详细编辑实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class AiChatDetailEditDTO extends BaseEditDTOEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话详细id")
    private Long chatDetailId;

    @Schema(description = "会话id")
    @NotNull(message = "会话id不能为空")
    private Long chatConversationId;

    @Schema(description = "消息类型（1代表user 2代表assistant 3代表system 4代表tool）")
    @NotNull(message = "消息类型（1代表user 2代表assistant 3代表system 4代表tool）不能为空")
    private Integer messageType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件")
    private String attachment;

}
