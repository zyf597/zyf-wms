package com.zyf.domain.form;

import com.zyf.domain.entity.ShipmentOrder;
import com.zyf.domain.vo.DeliveryVO;
import com.zyf.domain.vo.ItemVO;
import com.zyf.domain.vo.ShipmentOrderDetailVO;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentOrderFrom extends ShipmentOrder {
  // 出库单详情
  private List<ShipmentOrderDetailVO> details;
  // 所有商品
  private List<ItemVO> items;
  // 物流信息
  private List<DeliveryVO> delivery;
}
