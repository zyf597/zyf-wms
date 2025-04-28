package com.zyf.convert;

import com.zyf.domain.entity.InventoryHistory;
import org.apache.commons.lang3.SerializationUtils;
import org.mapstruct.Mapper;
import com.zyf.domain.entity.ReceiptOrderDetail;
import com.zyf.domain.vo.ReceiptOrderDetailVO;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * 入库单详情  ENTITY <=> VO / Form / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface ReceiptOrderDetailConvert  {
    List<ReceiptOrderDetailVO> dos2vos(List<ReceiptOrderDetail> list);

    List<ReceiptOrderDetail> vos2dos(List<ReceiptOrderDetailVO> details);

    @Mapping(target = "quantity", source = "realQuantity")
    InventoryHistory do2InventoryHistory(ReceiptOrderDetailVO it);

    default Collection<ReceiptOrderDetailVO> copyList(Collection<ReceiptOrderDetailVO> original) {
        List<ReceiptOrderDetailVO> deepCopy = new ArrayList<>();

        for (ReceiptOrderDetailVO element : original) {
            byte[] serializedObject = SerializationUtils.serialize(element);
            ReceiptOrderDetailVO clonedElement = SerializationUtils.deserialize(serializedObject);
            deepCopy.add(clonedElement);
        }

        return deepCopy;
    }

    /**
     * @param source DO
     * @return VO
     */
    ReceiptOrderDetailVO toVo(ReceiptOrderDetail source);
}
