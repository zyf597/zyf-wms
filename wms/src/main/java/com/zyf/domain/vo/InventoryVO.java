package com.zyf.domain.vo;

import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存 数据视图对象
 *
 * @author zyf
 */
@Data
public class InventoryVO extends BaseAudit implements AreaAndItemInfo {
    /**
     * ID
     */
    private Long id;
    /**
     * 物料ID
     */
    private Long itemId;
    // 物料编号
    @Excel(name = "物料编码")
    private String itemNo;
    // 物料名称
    @Excel(name = "物料名称")
    private String itemName;
    // 物料类型
    @Excel(name = "物料类型")
    private String itemTypeName;
    /**
     * 货架id
     */
    private Long rackId;
    // 货架 名称
    private String rackName;
    private Long warehouseId;
    // 仓库 名称
    @Excel(name = "仓库")
    private String warehouseName;
    private Long areaId;
    // 库区 名称
    @Excel(name = "库区")
    private String areaName;
    /**
     * 库存
     */
    @Excel(name = "库存")
    private BigDecimal quantity;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
    private BigDecimal saftyQuantity;

    /**
     * 物料删除标识
     */
    private Integer itemDelFlag;

    /**
     * 仓库删除标识
     */
    private Integer warehouseDelFlag;
}
