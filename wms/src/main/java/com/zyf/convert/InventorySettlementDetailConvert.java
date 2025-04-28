package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventorySettlementDetail;
import com.zyf.domain.vo.InventorySettlementDetailVO;
import java.util.List;
/**
 * 库存结算明细  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventorySettlementDetailConvert  {
    List<InventorySettlementDetailVO> dos2vos(List<InventorySettlementDetail> list);

    List<InventorySettlementDetail> vos2dos(List<InventorySettlementDetailVO> details);

    List<InventorySettlementDetailVO> toVos(List<InventorySettlementDetail> inventoryCheckDetails);
}
