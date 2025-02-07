package com.yxx.example.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * 代码生成测试对象 tb_test_user
 * 
 * @author yxx
 * @date 2025-02-07
 */
@ApiModel(value = "TbTestUser", description = "代码生成测试实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_test_user")
public class TbTestUser extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId("userId")
    private Long userId;

    @ApiModelProperty("用户名称")
    @TableField("username")
    @Excel(name = "用户名称")
    private String username;

    @ApiModelProperty("密码密码")
    @TableField("password")
    @Excel(name = "密码密码")
    private String password;

    @ApiModelProperty("用户手机")
    @TableField("mobile")
    @Excel(name = "用户手机")
    private String mobile;

}
