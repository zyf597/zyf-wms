package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.SupplierTransaction;
import com.zyf.domain.vo.SupplierTransactionVO;
import java.util.List;
/**
 * 供应商账户流水  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface SupplierTransactionConvert  {
    List<SupplierTransactionVO> dos2vos(List<SupplierTransaction> list);
}
