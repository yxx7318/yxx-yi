package com.yxx.business.example.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.core.domain.BaseQueryDtoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 测试用户QueryDto对象 tb_test_user
 *
 * @author yxx
 * @date 2025-07-17
 */
//@Schema(description = "测试用户查询条件实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@ParameterObject
public class TbTestUserQueryDto extends BaseQueryDtoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    @Schema(description = "注册时间")
    private Date registerTime;

}
