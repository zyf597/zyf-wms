package com.zyf.convert;

import com.zyf.domain.form.InventorySettlementFrom;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventorySettlement;
import com.zyf.domain.vo.InventorySettlementVO;
import java.util.List;
/**
 * 库存结算单  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventorySettlementConvert  {
    List<InventorySettlementVO> dos2vos(List<InventorySettlement> list);

    InventorySettlementFrom do2form(InventorySettlement inventorySettlement);
}
