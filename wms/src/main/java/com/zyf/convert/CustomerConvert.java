package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Customer;
import com.zyf.domain.vo.CustomerVO;
import java.util.List;
/**
 * 客户  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface CustomerConvert  {
    List<CustomerVO> dos2vos(List<Customer> list);
}
