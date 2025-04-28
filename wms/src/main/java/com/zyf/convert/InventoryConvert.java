package com.zyf.convert;

import com.zyf.domain.entity.InventoryHistory;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.Inventory;
import com.zyf.domain.vo.InventoryVO;
import java.util.List;
/**
 * 库存  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryConvert  {
    List<InventoryVO> dos2vos(List<Inventory> list);

    Inventory inventoryHistory2invertory(InventoryHistory it);
}
