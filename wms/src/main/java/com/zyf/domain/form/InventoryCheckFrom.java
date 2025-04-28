package com.zyf.domain.form;

import com.zyf.domain.entity.InventoryCheck;
import com.zyf.domain.vo.InventoryCheckDetailVO;
import com.zyf.domain.vo.ItemVO;
import lombok.Data;

import java.util.List;

/**
 * 盘库单据 数据视图对象
 *
 * @author zyf
 */
@Data
public class InventoryCheckFrom extends InventoryCheck {
    // 盘库单据详情
    private List<InventoryCheckDetailVO> details;
    // 所有商品
    private List<ItemVO> items;
}
