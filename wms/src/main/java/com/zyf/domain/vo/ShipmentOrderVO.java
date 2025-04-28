package com.zyf.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出库单 数据视图对象
 *
 * @author zyf
 */
@Data
public class ShipmentOrderVO extends BaseAudit {
  /**
   * ID
   */
  private Long id;
  /**
   * 出库单号，系统自动生成
   */
  @Excel(name = "出库单号，系统自动生成")
  private String shipmentOrderNo;
  /**
   * 出库类型
   */
  @Excel(name = "出库类型")
  private Integer shipmentOrderType;
  /**
   * 出库订单
   */
  @Excel(name = "出库订单")
  private String orderNo;
  /**
   * 客户
   */
  @Excel(name = "客户")
  private Long customerId;
  /**
   * 出库单状态
   */
  @Excel(name = "出库单状态")
  private Integer shipmentOrderStatus;
  /**
   * 审核状态
   */
  @Excel(name = "审核状态")
  private Integer checkStatus;
  /**
   * 审核人
   */
  @Excel(name = "审核人")
  private Long checkUserId;
  /**
   * 审核时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime checkTime;
  /**
   * 备注
   */
  @Excel(name = "备注")
  private String remark;

  @Excel(name = "波次号")
  private String waveNo;

  private Integer detailCount;

}
