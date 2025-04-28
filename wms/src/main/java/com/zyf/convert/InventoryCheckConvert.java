package com.zyf.convert;

import com.zyf.domain.form.InventoryCheckFrom;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventoryCheck;
import com.zyf.domain.vo.InventoryCheckVO;
import java.util.List;
/**
 * 库存盘点单据  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryCheckConvert  {
    List<InventoryCheckVO> dos2vos(List<InventoryCheck> list);

    InventoryCheckFrom do2form(InventoryCheck inventoryCheck);
}
