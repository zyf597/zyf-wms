package com.zyf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyf.constant.ReceiptOrderConstant;
import com.zyf.constant.ShipmentOrderConstant;
import com.zyf.domain.entity.InventoryHistory;
import com.zyf.domain.entity.InventorySettlementDetail;
import com.zyf.mapper.InventoryHistoryMapper;
import com.zyf.mapper.InventorySettlementDetailMapper;
import com.zyf.domain.query.InventorySettlementDetailQuery;
import com.zyf.domain.vo.InventoryVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存结算明细Service业务层处理
 *
 * @author zyf
 */
@Service
@Slf4j
public class InventorySettlementDetailService {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventorySettlementDetailMapper inventorySettlementDetailMapper;

    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    /**
     * 查询库存结算明细
     *
     * @param id 库存结算明细主键
     * @return 库存结算明细
     */
    public InventorySettlementDetail selectById(Long id) {
        return inventorySettlementDetailMapper.selectById(id);
    }

    /**
     * 查询库存结算明细列表
     *
     * @param query 查询条件
     * @param page  分页条件
     * @return 库存结算明细
     */
    public List<InventorySettlementDetail> selectList(InventorySettlementDetailQuery query, Pageable page) {
        if (page != null) {
            PageHelper.startPage(page.getPageNumber() + 1, page.getPageSize());
        }
        QueryWrapper<InventorySettlementDetail> qw = new QueryWrapper<>();
        qw.eq("del_flag", 0);
        Long settlementId = query.getSettlementId();
        if (settlementId != null) {
            qw.eq("settlement_id", settlementId);
        }
        Integer settlementType = query.getSettlementType();
        if (settlementType != null) {
            qw.eq("settlement_type", settlementType);
        }
        Long itemId = query.getItemId();
        if (itemId != null) {
            qw.eq("item_id", itemId);
        }
        String itemNo = query.getItemNo();
        if (!StringUtils.isEmpty(itemNo)) {
            qw.eq("item_no", itemNo);
        }
        String itemNameLike = query.getItemNameLike();
        if (!StringUtils.isEmpty(itemNameLike)) {
            qw.like("item_name", itemNameLike);
        }
        Long warehouseId = query.getWarehouseId();
        if (warehouseId != null) {
            qw.eq("warehouse_id", warehouseId);
        }
        String warehouseNo = query.getWarehouseNo();
        if (!StringUtils.isEmpty(warehouseNo)) {
            qw.eq("warehouse_no", warehouseNo);
        }
        String warehouseNameLike = query.getWarehouseNameLike();
        if (!StringUtils.isEmpty(warehouseNameLike)) {
            qw.like("warehouse_name", warehouseNameLike);
        }
        Long areaId = query.getAreaId();
        if (areaId != null) {
            qw.eq("area_id", areaId);
        }
        String areaNo = query.getAreaNo();
        if (!StringUtils.isEmpty(areaNo)) {
            qw.eq("area_no", areaNo);
        }
        String areaNameLike = query.getAreaNameLike();
        if (!StringUtils.isEmpty(areaNameLike)) {
            qw.like("area_name", areaNameLike);
        }
        BigDecimal previousBalance = query.getPreviousBalance();
        if (previousBalance != null) {
            qw.eq("previous_balance", previousBalance);
        }
        BigDecimal currentEnter = query.getCurrentEnter();
        if (currentEnter != null) {
            qw.eq("current_enter", currentEnter);
        }
        BigDecimal currentOut = query.getCurrentOut();
        if (currentOut != null) {
            qw.eq("current_out", currentOut);
        }
        BigDecimal currentCheck = query.getCurrentCheck();
        if (currentCheck != null) {
            qw.eq("current_check", currentCheck);
        }
        BigDecimal currentBalance = query.getCurrentBalance();
        if (currentBalance != null) {
            qw.eq("current_balance", currentBalance);
        }
        return inventorySettlementDetailMapper.selectList(qw);
    }

    /**
     * 新增库存结算明细
     *
     * @param inventorySettlementDetail 库存结算明细
     * @return 结果
     */
    public int insert(InventorySettlementDetail inventorySettlementDetail) {
        inventorySettlementDetail.setDelFlag(0);
        inventorySettlementDetail.setCreateTime(LocalDateTime.now());
        return inventorySettlementDetailMapper.insert(inventorySettlementDetail);
    }

    /**
     * 修改库存结算明细
     *
     * @param inventorySettlementDetail 库存结算明细
     * @return 结果
     */
    public int update(InventorySettlementDetail inventorySettlementDetail) {
        return inventorySettlementDetailMapper.updateById(inventorySettlementDetail);
    }

    /**
     * 批量删除库存结算明细
     *
     * @param ids 需要删除的库存结算明细主键
     * @return 结果
     */
    public int deleteByIds(Long[] ids) {
        return inventorySettlementDetailMapper.updateDelFlagByIds(ids);
    }

    /**
     * 删除库存结算明细信息
     *
     * @param id 库存结算明细主键
     * @return 结果
     */
    public int deleteById(Long id) {
        Long[] ids = {id};
        return inventorySettlementDetailMapper.updateDelFlagByIds(ids);
    }

    /**
     * 生成库存结算明细
     *
     * @param query 查询条件
     * @return 结果
     */
    public List<InventorySettlementDetail> listByTime(InventorySettlementDetailQuery query) {

        //获取本结算周期的库存历史记录表
        LambdaQueryWrapper<InventoryHistory> qw = new LambdaQueryWrapper<>();
        qw.eq(InventoryHistory::getDelFlag, 0);
        if (query.getStartTime() != null) {
            qw.between(InventoryHistory::getCreateTime, query.getStartTime(), query.getEndTime());
        }
        List<InventoryHistory> inventoryHistories = inventoryHistoryMapper.selectList(qw);

        //按照物料id_仓库id_库区id 分组
        Map<String, List<InventoryHistory>> inventoryHistoryMap = inventoryHistories
                .stream().collect(Collectors.groupingBy(it -> it.getItemId() + "_" + it.getWarehouseId() + "_" + it.getAreaId()));

        //获取上期结存表
        LambdaQueryWrapper<InventorySettlementDetail> settlementDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        settlementDetailLambdaQueryWrapper.eq(InventorySettlementDetail::getDelFlag, 0);
        settlementDetailLambdaQueryWrapper.eq(InventorySettlementDetail::getSettlementType, query.getSettlementType());
        settlementDetailLambdaQueryWrapper.orderByDesc(InventorySettlementDetail::getCreateTime);
        List<InventorySettlementDetail> inventorySettlementDetails = inventorySettlementDetailMapper.selectList(settlementDetailLambdaQueryWrapper);
        //按照物料id_仓库id_库区id 分组
        Map<String, List<InventorySettlementDetail>> previousInventorySettlementDetails = inventorySettlementDetails
                .stream().collect(Collectors.groupingBy(it -> it.getItemId() + "_" + it.getWarehouseId() + "_" + it.getAreaId()));


        List<InventorySettlementDetail> list = new LinkedList<>();
        List<InventoryVO> inventoryList = inventoryService.queryValidAll();
        log.info("有效库存：{}", inventoryList);
        inventoryList.forEach(inventoryVO -> {


            List<InventoryHistory> inventoryHistories1 = inventoryHistoryMap.get(inventoryVO.getItemId() + "_" + inventoryVO.getWarehouseId() + "_" + inventoryVO.getAreaId());

            if (CollectionUtils.isEmpty(inventoryHistories1)) {
                inventoryHistories1 = new ArrayList<>();
            }

            //获取入库的库存变化总和
            List<Integer> receipt = Arrays.asList(ReceiptOrderConstant.PURCHASE, ReceiptOrderConstant.OUTSOURCING, ReceiptOrderConstant.RETURN);
            BigDecimal enter = inventoryHistories1.stream().filter(it -> receipt.contains(it.getFormType())).map(InventoryHistory::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);

            //获取出库的库存变化总和
            List<Integer> shipment = Arrays.asList(ShipmentOrderConstant.SALE, ShipmentOrderConstant.OUTSOURCING, ShipmentOrderConstant.DEPT, ShipmentOrderConstant.CHECK_OUT);
            BigDecimal out = inventoryHistories1.stream().filter(it -> shipment.contains(it.getFormType())).map(InventoryHistory::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);

            //获取盘库的库存变化总和
            List<Integer> check = Arrays.asList(ReceiptOrderConstant.CHECK_IN, ShipmentOrderConstant.CHECK_OUT);
            BigDecimal currCheck = inventoryHistories1.stream().filter(it -> check.contains(it.getFormType())).map(InventoryHistory::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);

            //上期结存
            List<InventorySettlementDetail> previous = previousInventorySettlementDetails.get(inventoryVO.getItemId() + "_" + inventoryVO.getWarehouseId() + "_" + inventoryVO.getAreaId());
            BigDecimal previousBalance = BigDecimal.ZERO;
            if (!CollectionUtils.isEmpty(previous)) {
                previousBalance = previous.get(0).getCurrentBalance();
            }

            if (inventoryVO.getItemId() == null) {
                System.out.println("物料id为空" + inventoryVO);
                return;
            }
            InventorySettlementDetail inventorySettlementDetail = new InventorySettlementDetail();
            inventorySettlementDetail.setAreaId(inventoryVO.getAreaId());
            inventorySettlementDetail.setAreaName(inventoryVO.getAreaName());
            inventorySettlementDetail.setWarehouseId(inventoryVO.getWarehouseId());
            inventorySettlementDetail.setWarehouseName(inventoryVO.getWarehouseName());
            inventorySettlementDetail.setItemId(inventoryVO.getItemId());
            inventorySettlementDetail.setItemName(inventoryVO.getItemName());
            inventorySettlementDetail.setItemNo(inventoryVO.getItemNo());
            inventorySettlementDetail.setCurrentBalance(inventoryVO.getQuantity());
            inventorySettlementDetail.setCurrentOut(out.negate());
            inventorySettlementDetail.setCurrentEnter(enter);
            inventorySettlementDetail.setCurrentCheck(currCheck);
            inventorySettlementDetail.setPreviousBalance(previousBalance);
            log.info("库存结算明细：{}", inventorySettlementDetail);
            list.add(inventorySettlementDetail);
        });
        return list;
    }
}
