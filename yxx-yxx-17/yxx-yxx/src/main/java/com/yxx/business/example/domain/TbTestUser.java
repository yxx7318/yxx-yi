package com.yxx.business.example.domain;

import java.util.Date;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import jakarta.validation.constraints.NotNull;

/**
 * 测试用户对象 tb_test_user
 *
 * @author yxx
 * @date 2025-06-03
 */
@Schema(description = "测试用户实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "tb_test_user", autoResultMap = true)
public class TbTestUser extends BaseColumnEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Schema(description = "用户账号")
    @NotNull(message = "用户账号不能为空")
    @TableField("user_name")
    private String userName;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "注册时间")
    @TableField("register_time")
    private Date registerTime;

    @Schema(description = "账号状态（0正常 1停用）")
    @TableField("status")
    private String status;

}
