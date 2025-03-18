package com.yxx.example.domain;

import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 测试用户对象 tb_test_user
 *
 * @author yxx
 * @date 2025-03-18
 */
@ApiModel(value = "TbTestUser", description = "测试用户实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "tb_test_user", autoResultMap = true)
public class TbTestUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("用户账号")
    @TableField("user_name")
    @Excel(name = "用户账号")
    private String userName;

    @ApiModelProperty("密码")
    @TableField("password")
    @Excel(name = "密码")
    private String password;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    @TableField("status")
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

}
