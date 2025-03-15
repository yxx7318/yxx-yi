package com.yxx.system.domain;

import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 数据源对象 sys_resources
 *
 * @author yxx
 * @date 2025-03-15
 */
@ApiModel(value = "SysResources", description = "数据源实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@TableName(value = "sys_resources", autoResultMap = true)
public class SysResources extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据源id")
    @TableId(value = "resource_id", type = IdType.AUTO)
    private Long resourceId;

    @ApiModelProperty("数据源名称")
    @TableField("resource_name")
    @Excel(name = "数据源名称")
    private String resourceName;

    @ApiModelProperty("数据源编码")
    @TableField("code")
    @Excel(name = "数据源编码")
    private String code;

    @ApiModelProperty("数据源驱动")
    @TableField("driver_classname")
    @Excel(name = "数据源驱动")
    private String driverClassname;

    @ApiModelProperty("数据源地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("数据源用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("数据源密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("数据源状态")
    @TableField("status")
    @Excel(name = "数据源状态")
    private String status;

    @ApiModelProperty("删除标识")
    @TableField("del_flag")
    private String delFlag;

}
