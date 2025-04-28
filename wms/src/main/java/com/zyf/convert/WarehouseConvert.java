package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Warehouse;
import com.zyf.domain.vo.WarehouseVO;
import java.util.List;
/**
 * 仓库  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface WarehouseConvert  {
    List<WarehouseVO> dos2vos(List<Warehouse> list);
}
