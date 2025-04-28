package com.zyf.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 出库单详情对象 wms_shipment_order_detail
 *
 * @author zyf
 */
@ApiModel(description = "出库单详情对象")
@Data
@TableName("wms_shipment_order_detail")
public class ShipmentOrderDetail extends BaseAudit {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;
    /**
     * 订单号
     */
    @Excel(name = "订单号")
    @TableField(exist = false)
    private String orderNo;

    @ApiModelProperty("出库单")
    @Excel(name = "出库单")
    private Long shipmentOrderId;

    @ApiModelProperty("物料")
    @Excel(name = "物料")
    private Long itemId;

    @ApiModelProperty("计划数量")
    @Excel(name = "计划数量")
    private BigDecimal planQuantity;

    @ApiModelProperty("实际数量")
    @Excel(name = "实际数量")
    private BigDecimal realQuantity;

    @ApiModelProperty("货架")
    @Excel(name = "货架")
    private Long rackId;

    @ApiModelProperty("金额")
    @Excel(name = "金额")
    private BigDecimal money;

    @ApiModelProperty("删除标识")
    private Integer delFlag;

    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty("所属仓库")
    @Excel(name = "所属仓库")
    private Long warehouseId;

    @ApiModelProperty("所属库区")
    @Excel(name = "所属库区")
    private Long areaId;

    @ApiModelProperty("出库状态")
    @Excel(name = "出库状态")
    private Integer shipmentOrderStatus;

}
