package com.zyf.convert;

import com.zyf.domain.entity.InventoryHistory;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.InventoryCheckDetail;
import com.zyf.domain.vo.InventoryCheckDetailVO;
import java.util.List;
/**
 * 库存盘点单据详情  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface InventoryCheckDetailConvert  {
    List<InventoryCheckDetailVO> dos2vos(List<InventoryCheckDetail> list);

    List<InventoryCheckDetail> vos2dos(List<InventoryCheckDetailVO> details);

    InventoryHistory vo2InventoryHistory(InventoryCheckDetailVO it);
}
