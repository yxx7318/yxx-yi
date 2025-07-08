package com.yxx.business.example.domain;

import com.yxx.common.core.domain.BaseQueryDtoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 测试用户对象 tb_test_user
 *
 * @author yxx
 * @date 2025-06-03
 */
@Schema(description = "测试用户查询条件实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserQueryDto extends BaseQueryDtoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "注册时间")
    private Date registerTime;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

}
