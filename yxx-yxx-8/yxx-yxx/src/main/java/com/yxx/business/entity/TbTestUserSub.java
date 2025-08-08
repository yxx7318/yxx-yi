package com.yxx.business.entity;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 测试主表生成对象 tb_test_user_sub
 *
 * @author yxx
 * @date 2025-08-08
 */
@Schema(description = "测试主表生成实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "tb_test_user_sub", autoResultMap = true)
public class TbTestUserSub extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主表ID")
    @TableId(value = "sub_id", type = IdType.AUTO)
    private Long subId;

    @Schema(description = "用户账号")
    @NotBlank(message = "用户账号不能为空")
    @TableField("user_name")
    @Excel(name = "用户账号")
    private String userName;

    @Schema(description = "密码")
    @TableField("password")
    @Excel(name = "密码")
    private String password;

    @Schema(description = "账号状态（0正常 1停用）")
    @TableField("status")
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @Schema(description = "注册日期")
    @TableField("register_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date registerDate;

    @Schema(description = "注册时间")
    @TableField("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;

    /**
    * 测试单生成信息
    */
    @TableField(exist = false)
    @Valid
    private List<TbTestUser> tbTestUserList;

}
