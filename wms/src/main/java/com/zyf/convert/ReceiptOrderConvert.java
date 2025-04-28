package com.zyf.convert;

import com.zyf.domain.form.ReceiptOrderForm;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.ReceiptOrder;
import com.zyf.domain.vo.ReceiptOrderVO;
import java.util.List;
/**
 * 入库单  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface ReceiptOrderConvert  {
    List<ReceiptOrderVO> dos2vos(List<ReceiptOrder> list);

    ReceiptOrderForm do2form(ReceiptOrder bean);
}
