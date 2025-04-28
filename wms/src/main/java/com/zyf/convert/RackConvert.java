package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Rack;
import com.zyf.domain.vo.RackVO;
import java.util.List;
/**
 * 货架  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface RackConvert  {
    List<RackVO> dos2vos(List<Rack> list);
}
