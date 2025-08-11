package com.yxx.business.entity;

import java.util.Date;
import com.yxx.common.core.domain.BaseQueryDtoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 测试单表生成QueryDto对象 tb_test_user
 *
 * @author yxx
 * @date 2025-08-11
 */
@Schema(description = "测试单表生成查询条件实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserQueryDto extends BaseQueryDtoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "主表ID")
    private Long parentId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    @Schema(description = "注册日期")
    private Date registerDate;

    @Schema(description = "注册时间")
    private Date registerTime;

}
