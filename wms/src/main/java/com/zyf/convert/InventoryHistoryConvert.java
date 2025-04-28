package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventoryHistory;
import com.zyf.domain.vo.InventoryHistoryVO;
import java.util.List;
/**
 * 库存记录  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryHistoryConvert  {
    List<InventoryHistoryVO> dos2vos(List<InventoryHistory> list);
}
