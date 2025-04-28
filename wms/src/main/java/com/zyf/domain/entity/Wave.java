package com.zyf.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zyf.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;

/**
 * 波次对象 wms_wave
 *
 * @author zyf
 */
@ApiModel(description = "波次对象")
@Data
@TableName("wms_wave")
public class Wave extends BaseAudit {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("波次号")
    @Excel(name = "波次号")
    private String waveNo;

    @ApiModelProperty("状态")
    @Excel(name = "状态")
    private String status;

    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty("删除标识")
    private Integer delFlag;

    @ApiModelProperty("类型")
    @Excel(name = "类型")
    private Integer type;


    @TableField(exist = false)
    private ArrayList<Long> ids;

}
