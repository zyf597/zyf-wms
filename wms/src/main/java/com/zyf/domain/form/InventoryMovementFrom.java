package com.zyf.domain.form;

import com.zyf.domain.entity.InventoryMovement;
import com.zyf.domain.vo.InventoryMovementDetailVO;
import com.zyf.domain.vo.ItemVO;
import lombok.Data;

import java.util.List;

@Data
public class InventoryMovementFrom extends InventoryMovement {
  // 详情
  private List<InventoryMovementDetailVO> details;
  // 所有商品
  private List<ItemVO> items;
}
