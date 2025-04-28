package com.zyf.domain.form;

import com.zyf.domain.entity.InventorySettlement;
import com.zyf.domain.vo.InventorySettlementDetailVO;
import com.zyf.domain.vo.ItemVO;
import lombok.Data;

import java.util.List;

/**
 * 盘库结算 数据视图对象
 *
 * @author zyf
 */
@Data
public class InventorySettlementFrom extends InventorySettlement {
    // 盘库结算详情
    private List<InventorySettlementDetailVO> details;
    // 所有商品
    private List<ItemVO> items;
}
