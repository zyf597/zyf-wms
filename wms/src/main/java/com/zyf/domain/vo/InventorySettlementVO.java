package com.zyf.domain.vo;

import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
/**
 * 库存结算单 数据视图对象
 *
 * @author zyf
 */
@Data
public class InventorySettlementVO extends BaseAudit {
   /** ID */
    private Long id;
   /** 库存结算单状态11：结算中 22：已完成 */
    @Excel(name = "库存结算单状态11：结算中 22：已完成")
    private Integer inventorySettlementStatus;
   /** 结算类型,1:月结，2:年结 */
    @Excel(name = "结算类型,1:月结，2:年结")
    private Integer settlementType;
   /** 备注 */
    @Excel(name = "备注")
    private String remark;
   /** 库存结算单号 */
    @Excel(name = "库存结算单号")
    private String inventorySettlementNo;
}
