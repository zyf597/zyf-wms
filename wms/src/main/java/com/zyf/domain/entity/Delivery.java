package com.zyf.domain.entity;

import java.time.LocalDateTime;
import com.zyf.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 发货记录对象 wms_delivery
 *
 * @author zyf
 */
@ApiModel(description="发货记录对象")
@Data
@TableName("wms_delivery")
public class Delivery extends BaseAudit {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("出库单主表Id")
    @Excel(name = "出库单主表Id")
    private Long shipmentOrderId;

    @ApiModelProperty("承运商Id")
    @Excel(name = "承运商Id")
    private Long carrierId;

    @ApiModelProperty("发货日期")
    @Excel(name = "发货日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;

    @ApiModelProperty("快递单号")
    @Excel(name = "快递单号")
    private String trackingNo;

    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty("删除标识")
    private Integer delFlag;

}
