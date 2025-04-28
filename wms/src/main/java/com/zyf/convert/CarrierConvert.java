package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Carrier;
import com.zyf.domain.vo.CarrierVO;
import java.util.List;
/**
 * 承运商  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface CarrierConvert  {
    List<CarrierVO> dos2vos(List<Carrier> list);
}
