package com.yxx.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.core.domain.BaseEditDtoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 测试用户EditDto对象 tb_test_user
 *
 * @author yxx
 * @date 2025-08-04
 */
@Schema(description = "测试用户编辑实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserEditDto extends BaseEditDtoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户账号")
    @NotNull(message = "用户账号不能为空")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    @Schema(description = "注册时间")
    private Date registerTime;

}
