package com.zyf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyf.constant.ShipmentOrderConstant;
import com.zyf.convert.ShipmentOrderDetailConvert;
import com.zyf.mapper.ShipmentOrderDetailMapper;
import com.zyf.mapper.ShipmentOrderMapper;
import com.zyf.mapper.WaveMapper;
import com.zyf.domain.entity.InventoryHistory;
import com.zyf.domain.entity.ShipmentOrder;
import com.zyf.domain.entity.ShipmentOrderDetail;
import com.zyf.domain.entity.Wave;
import com.zyf.domain.query.WaveQuery;
import com.zyf.domain.vo.ShipmentOrderDetailVO;
import com.zyf.domain.form.OrderWaveFrom;
import com.github.pagehelper.PageHelper;
import com.zyf.common.exception.ServiceException;
import com.zyf.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 波次Service业务层处理
 *
 * @author zyf
 */
@Service
@Slf4j
public class WaveService {
    @Autowired
    private WaveMapper waveMapper;
    @Autowired
    private InventoryHistoryService inventoryHistoryService;
    @Autowired
    private ShipmentOrderService shipmentOrderService;
    @Autowired
    private ShipmentOrderDetailService shipmentOrderDetailService;
    @Autowired
    private ShipmentOrderDetailConvert shipmentOrderDetailConvert;


    @Autowired
    private ShipmentOrderMapper shipmentOrderMapper;

    @Autowired
    private ShipmentOrderDetailMapper shipmentOrderDetailMapper;
    @Autowired
    private InventoryService inventoryService;

    /**
     * 查询波次
     *
     * @param id 波次主键
     * @return 波次
     */
    public Wave selectById(Long id) {
        return waveMapper.selectById(id);
    }

    /**
     * 查询波次列表
     *
     * @param query 查询条件
     * @param page  分页条件
     * @return 波次
     */
    public List<Wave> selectList(WaveQuery query, Pageable page) {
        if (page != null) {
            PageHelper.startPage(page.getPageNumber() + 1, page.getPageSize());
        }
        QueryWrapper<Wave> qw = new QueryWrapper<>();
        qw.orderByDesc("id");
        qw.eq("del_flag", 0);
        String waveNo = query.getWaveNo();
        if (!StringUtils.isEmpty(waveNo)) {
            qw.eq("wave_no", waveNo);
        }
        String status = query.getStatus();
        if (!StringUtils.isEmpty(status)) {
            qw.eq("status", status);
        }
        if (query.getType() != null) {
            qw.eq("type", query.getType());
        }
        return waveMapper.selectList(qw);
    }

    /**
     * 新增波次
     *
     * @param wave 波次
     * @return 结果
     */
    public int insert(Wave wave) {
        wave.setDelFlag(0);
        wave.setCreateTime(LocalDateTime.now());
        return waveMapper.insert(wave);
    }

    /**
     * 新增波次
     *
     * @param wave 波次
     * @return 结果
     */
    @Transactional
    public int creatWave(Wave wave) {
        ArrayList<Long> orderIds = wave.getIds();
        log.info("出库单进行波次作业:{}", orderIds);
        wave.setWaveNo("WV" + System.currentTimeMillis());
        wave.setStatus("1");
        wave.setDelFlag(0);
        wave.setType(2);
        wave.setCreateTime(LocalDateTime.now());
        boolean flag = waveMapper.insert(wave) > 0;
        if (flag) {
            // 变更相应订单的波次号
            for (Long orderId : orderIds) {
                shipmentOrderService.updateWaveNo(orderId, wave.getWaveNo());
            }
        }
        return flag ? 1 : 0;
    }

    /**
     * 获取波次单明细
     *
     * @param id 波次id
     * @return 结果
     */
    @Transactional
    public OrderWaveFrom getShipmentOrders(long id) {
        Wave wave = selectById(id);
        if (wave == null || wave.getType() != 2) {
            throw new ServiceException("波次单不存在");
        }
        String waveNo = wave.getWaveNo();
        OrderWaveFrom orderWaveFrom = shipmentOrderService.selectDetailByWaveNo(waveNo);
        orderWaveFrom.setRemark(wave.getRemark());
        orderWaveFrom.setStatus(wave.getStatus());
        return orderWaveFrom;
    }


    /**
     * 修改波次
     *
     * @param wave 波次
     * @return 结果
     */
    public int update(Wave wave) {
        return waveMapper.updateById(wave);
    }

    /**
     * 批量删除波次
     *
     * @param ids 需要删除的波次主键
     * @return 结果
     */
    public int deleteByIds(Long[] ids) {
        shipmentOrderService.deleteByWaveIds(waveMapper.selectList(new LambdaQueryWrapper<Wave>()
                        .select(Wave::getWaveNo)
                        .in(Wave::getId, Arrays.asList(ids)))
                .stream().map(Wave::getWaveNo).collect(Collectors.toList()));
        return waveMapper.updateDelFlagByIds(ids);
    }

    /**
     * 删除波次信息
     *
     * @param id 波次主键
     * @return 结果
     */
    public int deleteById(Long id) {
        Long[] ids = {id};
        return waveMapper.updateDelFlagByIds(ids);
    }

    /*
     * 波次单分配仓库
     * */
    @Transactional
    public OrderWaveFrom allocatedInventory(Long id, Integer type) {
        log.info("波次单分配仓库分配仓库,波次单id:{}", id);
        Wave wave = selectById(id);
        String waveNo = wave.getWaveNo();
        OrderWaveFrom shipmentOrderFrom = shipmentOrderService.selectDetailByWaveNo(waveNo);
        shipmentOrderFrom.setStatus(wave.getStatus());
        shipmentOrderFrom.setRemark(wave.getRemark());
        List<ShipmentOrderDetailVO> originalDetail = (List<ShipmentOrderDetailVO>) shipmentOrderDetailConvert.copyList(shipmentOrderFrom.getDetails());

        List<ShipmentOrderDetailVO> originalDetails = aggregatedShipmentOrderDetailVOS(originalDetail);
        // 根据itemId分组，统计数量
        Map<Long, BigDecimal> collect = originalDetails.parallelStream().collect(Collectors.groupingBy(ShipmentOrderDetailVO::getItemId,
                Collectors.reducing(BigDecimal.ZERO, ShipmentOrderDetailVO::getPlanQuantity, BigDecimal::add)));

        List<ShipmentOrderDetail> allocationDetails = new ArrayList<>();

        //collect分配库存
        collect.forEach((itemId, quantity) -> {
            log.info("波次单分配仓库分配仓库,波次单id:{},物料id：{},数量：{}", id, itemId, quantity);
            List<ShipmentOrderDetail> shipmentOrderDetails = inventoryService.
                    allocatedInventory(itemId, quantity, type);
            allocationDetails.addAll(shipmentOrderDetails);
        });

        log.info("分配库存详情\n{}", allocationDetails);
        List<ShipmentOrderDetailVO> list = shipmentOrderDetailService.toVos(allocationDetails);
        List<ShipmentOrderDetailVO> allocatedDetails = new ArrayList<>();

        //2.更新原始库存
        originalDetails.forEach(originalOrder -> {
            Long itemId = originalOrder.getItemId();
            BigDecimal planQuantity = originalOrder.getPlanQuantity();
            // 从list里面取出相同物料得库存记录，需要取planQuantity数量，
            // 如果planQuantity数量小于库存数量，需要继续取下一个库存记录，直到planQuantity为0。
            // 已经被使用得list记录需要从list中移除
            for (int i = 0; i < list.size() && planQuantity.compareTo(BigDecimal.ZERO) > 0; i++) {
                ShipmentOrderDetailVO availableDetail = list.get(i);
                Long itemId1 = availableDetail.getItemId();
                BigDecimal availableQuantity = availableDetail.getPlanQuantity();
                if (itemId.equals(itemId1)) {
                    if (planQuantity.compareTo(availableQuantity) >= 0) {
                        //深拷贝一个新的对象
                        planQuantity = planQuantity.subtract(availableQuantity);
                        originalOrder.setPlanQuantity(planQuantity);
                        allocatedDetails.add(convert2vo(originalOrder, availableDetail, availableQuantity));
                        list.remove(i);
                        i--;
                    } else {
                        availableQuantity = availableQuantity.subtract(planQuantity);
                        // 更新list当前item得数量
                        availableDetail.setPlanQuantity(availableQuantity);
                        list.set(i, availableDetail);
                        allocatedDetails.add(convert2vo(originalOrder, availableDetail, planQuantity));
                        planQuantity = BigDecimal.ZERO;
                    }
                }
            }

        });
        //log.info("波次单分配仓库分配仓库,list:{}", list);
        //todo 2.针对波次分配失败的打包创建子波次(当前不具备拆分子波次功能)
        shipmentOrderFrom.setAllocationDetails(allocatedDetails);
        //log.info("波次单分配仓库分配仓库,originalDetails:{},分配库存详情\n{}", originalDetails, allocatedDetails);
        return shipmentOrderFrom;
    }


    /*
     * 取消分配 即还原出库单明细
     * */
    private static List<ShipmentOrderDetailVO> aggregatedShipmentOrderDetailVOS(List<ShipmentOrderDetailVO> originalDetail) {
        // 单个出库单分配后库区，还可以波次？ 可以，这一步就是重新聚合订单分散得拣货数据。
        // 聚合出库单，防止用户先前分配过库存。如果分配过库存，需要把分配过的库存加回来。保留原始订单信息
        Map<String, ShipmentOrderDetailVO> aggregatedDetails = new HashMap<>();
        originalDetail.forEach(vo -> {
            String key = vo.getItemId() + "_" + vo.getOrderNo();
            aggregatedDetails.merge(key, vo, (existingVo, newVo) -> {
                existingVo.setPlanQuantity(existingVo.getPlanQuantity().add(newVo.getPlanQuantity()));
                existingVo.setDelFlag(0);
                return existingVo;
            });
        });
        return new ArrayList<>(aggregatedDetails.values());
    }


    private ShipmentOrderDetailVO convert2vo(ShipmentOrderDetailVO originalOrder, ShipmentOrderDetailVO availableDetail, BigDecimal availableQuantity) {
        ShipmentOrderDetailVO shipmentOrderDetailVO2 = new ShipmentOrderDetailVO();
        shipmentOrderDetailVO2.setOrderNo(originalOrder.getOrderNo());
        shipmentOrderDetailVO2.setItemId(originalOrder.getItemId());
        shipmentOrderDetailVO2.setShipmentOrderId(originalOrder.getShipmentOrderId());
        shipmentOrderDetailVO2.setPlanQuantity(availableQuantity);
        shipmentOrderDetailVO2.setRealQuantity(availableQuantity);
        shipmentOrderDetailVO2.setWarehouseId(availableDetail.getWarehouseId());
        shipmentOrderDetailVO2.setAreaId(availableDetail.getAreaId());
        shipmentOrderDetailVO2.setPlace(availableDetail.getPlace());
        shipmentOrderDetailVO2.setDelFlag(0);
        // 默认出库状态为未出库
        shipmentOrderDetailVO2.setShipmentOrderStatus(ShipmentOrderConstant.NOT_IN);
        return shipmentOrderDetailVO2;
    }



    @Transactional
    public Integer confirmWave(OrderWaveFrom order) {
        //获取波次单
        Wave wave = waveMapper.selectById(order.getId());
        //数据库中的详情
        Map<Long, ShipmentOrderDetailVO> dbDetailMap = order.getDetails().stream().collect(Collectors.toMap(it->it.getId(), it->it));


        List<ShipmentOrderDetailVO> details = order.getAllocationDetails();
        // 删除出库单明细表
        List<Long> orderIds = details.stream().map(ShipmentOrderDetailVO::getShipmentOrderId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<ShipmentOrderDetail> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(ShipmentOrderDetail::getShipmentOrderId, orderIds);
        shipmentOrderDetailMapper.delete(deleteWrapper);

        // 插入出库单明细表
        List<ShipmentOrderDetail> shipmentOrderDetails = shipmentOrderDetailConvert.vos2dos(details);

        List<InventoryHistory> adds = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.getUserId();

        //获取订单
        Map<Long, ShipmentOrder> orderMap1 = shipmentOrderMapper.selectList(new QueryWrapper<ShipmentOrder>().in("id", details.stream().map(it->it.getShipmentOrderId()).collect(Collectors.toSet()))).stream().collect(Collectors.toMap(it -> it.getId(), it -> it));


        details.forEach(it -> {
            Integer status = it.getShipmentOrderStatus();
            if (status == ShipmentOrderConstant.NOT_IN || status == ShipmentOrderConstant.DROP) {
                // 未出库，跳过
                return;
            }
            ShipmentOrderDetailVO dbObj = dbDetailMap.get(it.getId());
            if (dbObj == null || !Objects.equals(dbObj.getShipmentOrderStatus(),status) || !Objects.equals(it.getRealQuantity(), dbObj.getRealQuantity())) {
                //计算真实的入库数量
                BigDecimal added;
                if (dbObj == null || dbObj.getShipmentOrderStatus() == ShipmentOrderConstant.NOT_IN) {
                    added = it.getRealQuantity();
                } else {
                    added = it.getRealQuantity().subtract(dbObj.getRealQuantity());
                }
                InventoryHistory h = shipmentOrderDetailConvert.do2InventoryHistory(it);
                h.setFormId(order.getId());
                h.setQuantity(added.negate());
                h.setFormType(orderMap1.get(it.getShipmentOrderId()).getShipmentOrderType());
                h.setDelFlag(0);
                h.setId(null);
                h.setCreateTime(now);
                h.setCreateBy(userId);
                adds.add(h);
            }
        });
        if (!adds.isEmpty()) {
            int add1 = inventoryHistoryService.batchInsert(adds);
//            adds.forEach(it -> it.setQuantity(it.getQuantity().negate()));
            int update1 = inventoryService.batchUpdate1(adds);
            log.info("inventoryHistory: {}, inventory: {}", add1, update1);
        }
        int batchInsert = shipmentOrderDetailMapper.batchInsert(shipmentOrderDetails);

        //先按照出库单ID分组
        Map<Long, List<ShipmentOrderDetail>> map = shipmentOrderDetails.stream().collect(Collectors.groupingBy(it -> it.getShipmentOrderId()));
        Map<Long, ShipmentOrder> orderMap = shipmentOrderMapper.selectList(new QueryWrapper<ShipmentOrder>().in("id", map.keySet())).stream().collect(Collectors.toMap(it -> it.getId(), it -> it));
        // 2.2 更新出库单
        AtomicReference<Boolean> finish = new AtomicReference<>(true);
        map.forEach((key, val) -> {
            //判断出库单的整体状态
            Set<Integer> statusList = val.stream().map(ShipmentOrderDetail::getShipmentOrderStatus).collect(Collectors.toSet());
            ShipmentOrder shipmentOrder = orderMap.get(key);
            if (statusList.size() == 1) {
                shipmentOrder.setShipmentOrderStatus(statusList.iterator().next());
            } else if (statusList.size() == 2) {
                if (statusList.contains(ShipmentOrderConstant.DROP) && statusList.contains(ShipmentOrderConstant.ALL_IN)) {
                    //此时单据状态只有报废和全部出库，则出库单状态为全部出库
                    shipmentOrder.setShipmentOrderStatus(ShipmentOrderConstant.ALL_IN);
                } else if (statusList.contains(ShipmentOrderConstant.PART_IN) || statusList.contains(ShipmentOrderConstant.ALL_IN)) {
                    //此时单据状态有两个，包含部分出库和全部出库都是部分出库
                    shipmentOrder.setShipmentOrderStatus(ShipmentOrderConstant.PART_IN);
                }

            } else if (statusList.contains(ShipmentOrderConstant.PART_IN) || statusList.contains(ShipmentOrderConstant.ALL_IN)) {
                //此时单据状态有两个，包含部分出库和全部出库都是部分出库
                shipmentOrder.setShipmentOrderStatus(ShipmentOrderConstant.PART_IN);
            }
            if (finish.get()) {
                finish.set(shipmentOrder.getShipmentOrderStatus() == ShipmentOrderConstant.ALL_IN);
            }
            shipmentOrderMapper.updateById(shipmentOrder);
        });
        //判断是否开始出库
        Set<Integer> totalStatusList = shipmentOrderDetails.stream().map(it -> it.getShipmentOrderStatus()).collect(Collectors.toSet());
        if (totalStatusList.size() == 1 && totalStatusList.contains(ShipmentOrderConstant.NOT_IN)) {
            wave.setStatus("1");
        } else {
            wave.setStatus(finish.get() ? "3" : "2");
        }
        if (!Objects.equals(order.getRemark(), wave.getRemark())) {
            wave.setRemark(order.getRemark());
        }
        waveMapper.updateById(wave);
        return batchInsert;

    }

    /*
     * 取消波次作业
     * */
    @Transactional
    public Integer cancelAllocatedInventory(Long id) {
        Wave wave = selectById(id);
        String waveNo = wave.getWaveNo();
        OrderWaveFrom shipmentOrderFrom = shipmentOrderService.selectDetailByWaveNo(waveNo);
        List<ShipmentOrderDetailVO> originalDetail = (List<ShipmentOrderDetailVO>) shipmentOrderDetailConvert.copyList(shipmentOrderFrom.getDetails());

        List<ShipmentOrderDetailVO> originalDetails = aggregatedShipmentOrderDetailVOS(originalDetail);
        // 删除出库单明细表
        List<Long> orderIds = originalDetail.stream().map(ShipmentOrderDetailVO::getShipmentOrderId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<ShipmentOrderDetail> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(ShipmentOrderDetail::getShipmentOrderId, orderIds);
        shipmentOrderDetailMapper.delete(deleteWrapper);

        // 插入出库单明细表
        List<ShipmentOrderDetail> shipmentOrderDetails = shipmentOrderDetailConvert.vos2dos(originalDetails);
        shipmentOrderDetails.forEach(shipmentOrderDetail -> {
            shipmentOrderDetail.setDelFlag(0);
            shipmentOrderDetail.setShipmentOrderStatus(ShipmentOrderConstant.NOT_IN);
        });
        return shipmentOrderDetailMapper.batchInsert(shipmentOrderDetails);
    }
}
