package com.yxx.business.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 测试树表生成对象 tb_test_user_tree
 *
 * @author yxx
 * @date 2025-10-13
 */
@Schema(description = "测试树表生成实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "tb_test_user_tree", autoResultMap = true)
public class TbTestUserTree extends TreeEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Schema(description = "节点ID")
    @TableField("tree_id")
    @Excel(name = "节点ID")
    private Long treeId;

    @Schema(description = "节点名称")
    @NotBlank(message = "节点名称不能为空")
    @TableField("tree_name")
    @Excel(name = "节点名称")
    private String treeName;

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
    private LocalDate registerDate;

    @Schema(description = "注册时间")
    @TableField("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;

}
