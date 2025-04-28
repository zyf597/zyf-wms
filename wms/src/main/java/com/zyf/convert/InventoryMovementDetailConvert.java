package com.zyf.convert;

import com.zyf.domain.entity.InventoryHistory;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventoryMovementDetail;
import com.zyf.domain.vo.InventoryMovementDetailVO;
import org.mapstruct.Mapping;

import java.util.List;
/**
 * 库存移动详情  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryMovementDetailConvert  {
    List<InventoryMovementDetailVO> dos2vos(List<InventoryMovementDetail> list);

    List<InventoryMovementDetail> vos2dos(List<InventoryMovementDetailVO> details);

    @Mapping(target = "quantity", source = "realQuantity")
    InventoryHistory do2InventoryHistory(InventoryMovementDetailVO it);
}
