package com.zyf.domain.form;

import com.zyf.domain.vo.ItemVO;
import com.zyf.domain.vo.ShipmentOrderDetailVO;
import com.zyf.domain.vo.WaveVO;
import lombok.Data;

import java.util.List;
/**
 * 波次 数据视图对象
 *
 * @author zyf
 */
@Data
public class OrderWaveFrom extends WaveVO {
  // 出库单详情
  private List<ShipmentOrderDetailVO> details;
  private List<ShipmentOrderDetailVO> allocationDetails;
  // 所有商品
  private List<ItemVO> items;
}
