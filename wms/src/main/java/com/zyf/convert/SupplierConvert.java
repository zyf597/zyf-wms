package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Supplier;
import com.zyf.domain.vo.SupplierVO;
import java.util.List;
/**
 * 供应商  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface SupplierConvert  {
    List<SupplierVO> dos2vos(List<Supplier> list);
}
