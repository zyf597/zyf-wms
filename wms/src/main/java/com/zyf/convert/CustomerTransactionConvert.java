package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.CustomerTransaction;
import com.zyf.domain.vo.CustomerTransactionVO;
import java.util.List;
/**
 * 客户账户流水  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface CustomerTransactionConvert  {
    List<CustomerTransactionVO> dos2vos(List<CustomerTransaction> list);
}
