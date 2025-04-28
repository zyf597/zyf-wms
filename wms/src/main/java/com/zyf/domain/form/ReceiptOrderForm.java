package com.zyf.domain.form;

import com.zyf.domain.entity.ReceiptOrder;
import com.zyf.domain.vo.ItemVO;
import com.zyf.domain.vo.ReceiptOrderDetailVO;
import lombok.Data;

import java.util.List;

@Data
public class ReceiptOrderForm extends ReceiptOrder {
    // 入库单详情
    private List<ReceiptOrderDetailVO> details;
    // 所有商品
    private List<ItemVO> items;
}
