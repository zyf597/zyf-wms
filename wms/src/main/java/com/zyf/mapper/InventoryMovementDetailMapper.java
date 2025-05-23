package com.zyf.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.domain.vo.InventoryMovementVO;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.InventoryMovementDetail;

/**
 * 库存移动详情Mapper接口
 *
 * @author zyf
 */
public interface InventoryMovementDetailMapper extends BaseMapper<InventoryMovementDetail> {
  /**
   * 查询库存移动详情列表
   *
   * @param inventoryMovementDetail 库存移动详情
   * @return 库存移动详情集合
   */
  List<InventoryMovementDetail> selectByEntity(InventoryMovementDetail inventoryMovementDetail);

  /**
   * 批量软删除
   *
   * @param ids
   * @return
   */
  int updateDelFlagByIds(@Param("ids") Long[] ids);

  List<InventoryMovementVO> countByOrderId(List<Long> ids);

  int batchInsert(List<InventoryMovementDetail> inventoryMovementDetails);
}
