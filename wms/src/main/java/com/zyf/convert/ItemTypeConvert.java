package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.ItemType;
import com.zyf.domain.vo.ItemTypeVO;
import java.util.List;
/**
 * 物料类型表  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface ItemTypeConvert  {
    List<ItemTypeVO> dos2vos(List<ItemType> list);
}
