package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Delivery;
import com.zyf.domain.vo.DeliveryVO;
import java.util.List;
/**
 * 发货记录  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface DeliveryConvert  {
    List<DeliveryVO> dos2vos(List<Delivery> list);
}
