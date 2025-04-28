package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Item;
import com.zyf.domain.vo.ItemVO;
import java.util.List;
/**
 * 物料  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface ItemConvert  {
    List<ItemVO> dos2vos(List<Item> list);

    ItemVO toVo(Item source);
}
