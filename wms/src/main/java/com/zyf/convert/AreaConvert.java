package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Area;
import com.zyf.domain.vo.AreaVO;
import java.util.List;
/**
 * 库区  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface AreaConvert  {
    List<AreaVO> dos2vos(List<Area> list);
}
