package com.yxx.business.entity;

import java.io.Serial;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 测试用户Vo对象 tb_test_user
 *
 * @author yxx
 * @date 2025-08-08
 */
@Schema(description = "测试用户Vo实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserVo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "父表ID")
    @Excel(name = "父表ID")
    private Long parentId;

    @Schema(description = "用户账号")
    @Excel(name = "用户账号")
    private String userName;

    @Schema(description = "密码")
    @Excel(name = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @Schema(description = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;

    @Schema(description = "注册日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date registerDate;

}
