package com.yxx.business.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.core.domain.BaseEditDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import jakarta.validation.constraints.NotBlank;

/**
 * 测试单表生成EditDTO对象 tb_test_user
 *
 * @author yxx
 * @date 2025-11-24
 */
@Schema(description = "测试单表生成编辑实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserEditDTO extends BaseEditDTOEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主表ID")
    private Long parentId;

    @Schema(description = "用户账号")
    @NotBlank(message = "用户账号不能为空")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    @Schema(description = "注册日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registerDate;

    @Schema(description = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;

}
