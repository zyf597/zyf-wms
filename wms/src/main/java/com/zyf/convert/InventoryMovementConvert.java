package com.zyf.convert;

import com.zyf.domain.form.InventoryMovementFrom;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventoryMovement;
import com.zyf.domain.vo.InventoryMovementVO;
import java.util.List;
/**
 * 库存移动  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryMovementConvert  {
    List<InventoryMovementVO> dos2vos(List<InventoryMovement> list);

    InventoryMovementFrom do2form(InventoryMovement order);
}
