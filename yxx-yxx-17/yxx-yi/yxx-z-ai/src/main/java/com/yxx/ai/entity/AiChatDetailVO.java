package com.yxx.ai.entity;

import java.io.Serial;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 会话详细VO对象 ai_chat_detail
 *
 * @author yxx
 * @date 2026-01-21
 */
@Schema(description = "会话详细VO实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class AiChatDetailVO extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话详细id")
    private Long chatDetailId;

    @Schema(description = "会话id")
    @Excel(name = "会话id")
    private Long chatConversationId;

    @Schema(description = "消息类型（1代表user 2代表assistant 3代表system 4代表tool）")
    @Excel(name = "消息类型", readConverterExp = "1=代表user,2=代表assistant,3=代表system,4=代表tool")
    private Integer messageType;

    @Schema(description = "内容")
    @Excel(name = "内容")
    private String content;

    @Schema(description = "附件")
    @Excel(name = "附件")
    private String attachment;

}
