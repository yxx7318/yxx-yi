package com.yxx.business.example.domain;

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

import javax.validation.constraints.NotNull;

/**
 * 测试用户Vo对象 tb_test_user
 *
 * @author yxx
 * @date 2025-07-17
 */
@Schema(description = "测试用户Vo实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class TbTestUserVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户账号")
    @NotNull(message = "用户账号不能为空")
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

}
