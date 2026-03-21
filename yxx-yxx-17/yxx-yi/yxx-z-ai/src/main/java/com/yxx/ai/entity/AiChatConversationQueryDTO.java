package com.yxx.ai.entity;

import java.io.Serial;
import com.yxx.common.core.domain.BaseQueryDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 会话QueryDTO对象 ai_chat_conversation
 *
 * @author yxx
 * @date 2026-01-21
 */
@Schema(description = "会话查询条件实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class AiChatConversationQueryDTO extends BaseQueryDTOEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话id")
    private Long chatConversationId;

    @Schema(description = "会话分组")
    private String chatGroup;

    @Schema(description = "会话标题")
    private String chatTitle;

}
