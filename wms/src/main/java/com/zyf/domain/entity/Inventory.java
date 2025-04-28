package com.zyf.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zyf.domain.vo.PlaceAndItem;
import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存对象 wms_inventory
 *
 * @author zyf
 */
@ApiModel(description = "库存对象")
@Data
@TableName("wms_inventory")
public class Inventory extends BaseAudit implements PlaceAndItem {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("物料ID")
    @Excel(name = "物料ID")
    private Long itemId;

    @ApiModelProperty("所属仓库")
    @Excel(name = "所属仓库")
    private Long warehouseId;

    @ApiModelProperty("所属库区")
    @Excel(name = "所属库区")
    private Long areaId;

    @ApiModelProperty("货架id")
    @Excel(name = "货架id")
    private Long rackId;

    @ApiModelProperty("库存")
    @Excel(name = "库存")
    private BigDecimal quantity;

    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty("删除标识")
    private Integer delFlag;

    @TableField(exist = false)
    private String itemNo;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String warehouseName;

    @TableField(exist = false)
    private String areaName;

}
