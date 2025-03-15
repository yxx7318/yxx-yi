package com.yxx.tenantA.domain;

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
 * xxx业务对象 business
 *
 * @author yxx
 * @date 2025-03-15
 */
@ApiModel(value = "Business", description = "xxx业务实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@TableName(value = "business", autoResultMap = true)
public class Business extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("业务id")
    @TableId(value = "business_id", type = IdType.AUTO)
    private Long businessId;

    @ApiModelProperty("详情")
    @TableField("detail")
    @Excel(name = "详情")
    private String detail;

}
