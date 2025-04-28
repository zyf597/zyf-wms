package com.zyf.convert;

import com.zyf.domain.form.ShipmentOrderFrom;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.ShipmentOrder;
import com.zyf.domain.vo.ShipmentOrderVO;
import java.util.List;
/**
 * 出库单  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface ShipmentOrderConvert  {
    List<ShipmentOrderVO> dos2vos(List<ShipmentOrder> list);

    ShipmentOrderFrom do2form(ShipmentOrder bean);
}
