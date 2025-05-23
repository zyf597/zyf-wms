package com.zyf.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyf.constant.InventoryMovementConstant;
import com.zyf.convert.InventoryMovementConvert;
import com.zyf.convert.InventoryMovementDetailConvert;
import com.zyf.domain.entity.InventoryHistory;
import com.zyf.domain.entity.InventoryMovement;
import com.zyf.domain.entity.InventoryMovementDetail;
import com.zyf.domain.entity.Item;
import com.zyf.mapper.InventoryMovementDetailMapper;
import com.zyf.mapper.InventoryMovementMapper;
import com.zyf.domain.query.InventoryMovementDetailQuery;
import com.zyf.domain.query.InventoryMovementQuery;
import com.zyf.domain.query.ItemQuery;
import com.zyf.domain.vo.InventoryMovementDetailVO;
import com.zyf.domain.vo.InventoryMovementVO;
import com.zyf.domain.vo.ItemVO;
import com.zyf.domain.form.InventoryMovementFrom;
import com.github.pagehelper.PageHelper;
import com.zyf.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存移动Service业务层处理
 *
 * @author zyf
 */
@Service
@Slf4j
public class InventoryMovementService {
    @Autowired
    private InventoryMovementMapper inventoryMovementMapper;
    @Autowired
    private InventoryMovementConvert convert;
    @Autowired
    private InventoryMovementDetailMapper inventoryMovementDetailMapper;
    @Autowired
    private InventoryMovementDetailService inventoryMovementDetailService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private InventoryMovementDetailConvert detailConvert;
    @Autowired
    private InventoryHistoryService inventoryHistoryService;
    @Autowired
    private InventoryService inventoryService;


    /**
     * 查询库存移动
     *
     * @param id 库存移动主键
     * @return 库存移动
     */
    public InventoryMovementFrom selectById(Long id) {

        InventoryMovement order = inventoryMovementMapper.selectById(id);
        if (order == null) {
            return null;
        }
        InventoryMovementDetailQuery query = new InventoryMovementDetailQuery();
        query.setInventoryMovementId(id);
        InventoryMovementFrom form = convert.do2form(order);
        List<InventoryMovementDetail> details = inventoryMovementDetailService.selectList(query, null);
        List<InventoryMovementDetailVO> detailVOS = inventoryMovementDetailService.toVos(details);
        form.setDetails(detailVOS);
        if (!CollUtil.isEmpty(form.getDetails())) {
            List<Long> itemIds = form.getDetails().stream().map(InventoryMovementDetailVO::getItemId).distinct().collect(Collectors.toList());
            ItemQuery query1 = new ItemQuery();
            query1.setIds(itemIds);
            List<Item> list = itemService.selectList(query1, null);
            List<ItemVO> items = itemService.toVos(list);
            form.setItems(items);
        }
        return form;
    }

    /**
     * 查询库存移动列表
     *
     * @param query 查询条件
     * @param page  分页条件
     * @return 库存移动
     */
    public Page<InventoryMovementVO> selectList(InventoryMovementQuery query, Pageable page) {
        if (page != null) {
            PageHelper.startPage(page.getPageNumber() + 1, page.getPageSize());
        }
        QueryWrapper<InventoryMovement> qw = new QueryWrapper<>();
        qw.orderByDesc("id");
        qw.eq("del_flag", 0);
        String inventoryMovementNo = query.getInventoryMovementNo();
        if (!StringUtils.isEmpty(inventoryMovementNo)) {
            qw.eq("inventory_movement_no", inventoryMovementNo);
        }
        Integer status = query.getStatus();
        if (status != null) {
            qw.eq("status", status);
        }
        List<InventoryMovement> list = inventoryMovementMapper.selectList(qw);
        if (CollectionUtils.isEmpty(list)) {
            return new PageImpl<>(Collections.EMPTY_LIST, page, 0);
        }
        List<InventoryMovementVO> res = convert.dos2vos(list);
        if (!CollUtil.isEmpty(res)) {
            List<Long> ids = res.stream().map(InventoryMovementVO::getId).collect(Collectors.toList());
            Map<Long, InventoryMovementVO> id2count = inventoryMovementDetailMapper.countByOrderId(ids)
                    .stream().collect(Collectors.toMap(InventoryMovementVO::getId, it -> it));
            res.forEach(it -> {
                InventoryMovementVO vo = id2count.get(it.getId());
                if (vo != null) {
                    it.setDetailCount(vo.getDetailCount());
                    it.setItemCount(vo.getItemCount());
                }
            });
        }
        return new PageImpl<>(res, page, ((com.github.pagehelper.Page) list).getTotal());
    }

    /**
     * 新增库存移动
     *
     * @param inventoryMovement 库存移动
     * @return 结果
     */
    public int insert(InventoryMovement inventoryMovement) {
        inventoryMovement.setDelFlag(0);
        inventoryMovement.setCreateTime(LocalDateTime.now());
        return inventoryMovementMapper.insert(inventoryMovement);
    }

    /**
     * 修改库存移动
     *
     * @param inventoryMovement 库存移动
     * @return 结果
     */
    public int update(InventoryMovement inventoryMovement) {
        return inventoryMovementMapper.updateById(inventoryMovement);
    }

    /**
     * 批量删除库存移动
     *
     * @param ids 需要删除的库存移动主键
     * @return 结果
     */
    @Transactional
    public int deleteByIds(Long[] ids) {
        int flag = 0;
        for (Long id : ids) {
            InventoryMovement inventoryMovement = inventoryMovementMapper.selectById(id);
            if (inventoryMovement == null) {
                continue;
            }
            Integer inventoryMovementStatus = inventoryMovement.getStatus();
            // 1. 逻辑删除移库单
            flag += inventoryMovementMapper.updateDelFlagByIds(ids);
            // 2. 逻辑删除移库单详情
            inventoryMovementDetailService.updateDelFlag(inventoryMovement);

            if (inventoryMovementStatus != InventoryMovementConstant.ALL_IN && inventoryMovementStatus != InventoryMovementConstant.PART_IN) {
                // 未移库的可以直接删除
                continue;
            }

            // 3. 查询库存记录
            List<InventoryHistory> inventoryHistories = inventoryHistoryService.selectByForm(inventoryMovement.getId(),
                    InventoryMovementConstant.IN_TYPE, InventoryMovementConstant.OUT_TYPE);

            // 翻转库存记录的数量
            inventoryHistories.forEach(it -> {
                it.setQuantity(it.getQuantity().negate());
                log.info("删除移库单：{} 回滚库存:{} 数量：{}", inventoryMovement.getInventoryMovementNo(),
                        it.getWarehouseId() + "_" + it.getAreaId() + "_" + it.getRackId() + "_" + it.getItemId(), it.getQuantity());
            });

            // 4. 回滚库存
            inventoryService.batchUpdate1(inventoryHistories);

            // 5. 删除库存记录
            inventoryHistoryService.deleteByForm(inventoryMovement.getId(), InventoryMovementConstant.IN_TYPE, InventoryMovementConstant.OUT_TYPE);

            // todo 6. 回滚供应商流水
//            supplierTransactionService.deleteByForm(inventoryMovement.getId(), inventoryMovement.getReceiptOrderType());
        }
        return flag;
    }

    /**
     * 删除库存移动信息
     *
     * @param id 库存移动主键
     * @return 结果
     */
    public int deleteById(Long id) {
        Long[] ids = {id};
        return inventoryMovementMapper.updateDelFlagByIds(ids);
    }

    @Transactional
    public int addOrUpdate(InventoryMovementFrom order) {
        int res;
        // 1. 新增
        if (order.getId() == null) {
            order.setDelFlag(0);
            order.setCreateTime(LocalDateTime.now());
            res = inventoryMovementMapper.insert(order);
            saveDetails(order.getId(), order.getDetails());
            return res;
        }
        // 2. 编辑
        QueryWrapper<InventoryMovementDetail> qw = new QueryWrapper<>();
        qw.eq("inventory_movement_id", order.getId());

        // 新旧详情对比， 生成 库存记录修改
        List<InventoryMovementDetailVO> details = order.getDetails();
        Map<Long, InventoryMovementDetail> dbDetailMap = inventoryMovementDetailMapper.selectList(qw).stream().collect(Collectors.toMap(InventoryMovementDetail::getId, it -> it));
        List<InventoryHistory> inList = new ArrayList<>();
        List<InventoryHistory> outList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.getUserId();
        details.forEach(it -> {
            Integer status = it.getMoveStatus();
            if (status != InventoryMovementConstant.PART_IN && status != InventoryMovementConstant.ALL_IN) {
                return;
            }
            // 新增时， status一定是未操作， 所以这个地方必定有值
            InventoryMovementDetail dbDetail = dbDetailMap.get(it.getId());
            // 如果上次的状态不是部分移动或者全部移动，则本次的库存变化为本次的全部
            Integer status1 = dbDetail.getMoveStatus();
            BigDecimal added;

            if (status1 != InventoryMovementConstant.PART_IN && status1 != InventoryMovementConstant.ALL_IN) {
                added = it.getRealQuantity();
            } else {
                BigDecimal before = dbDetail.getRealQuantity() == null ? BigDecimal.ZERO : dbDetail.getRealQuantity();
                BigDecimal after = it.getRealQuantity() == null ? BigDecimal.ZERO : it.getRealQuantity();
                // 数量变化有问题
                if (before.compareTo(after) >= 0) {
                    return;
                }
                added = after.subtract(before);
            }
            //判断库存是否足够出库
            inventoryService.checkInventory(it.getItemId(), it.getSourceWarehouseId(), it.getSourceAreaId(), it.getSourceRackId(), added);

            // 1. 创建移库日志
            InventoryHistory h = detailConvert.do2InventoryHistory(it);
            h.setFormId(order.getId());
            h.setFormType(InventoryMovementConstant.IN_TYPE);
            h.setRackId(it.getTargetRackId());
            h.setWarehouseId(it.getTargetWarehouseId());
            h.setAreaId(it.getTargetAreaId());
            h.setQuantity(added);
            h.setDelFlag(0);
            h.setId(null);
            h.setCreateTime(now);
            h.setCreateBy(userId);
            inList.add(h);

            // 出库日志
            InventoryHistory out = new InventoryHistory();
            BeanUtils.copyProperties(h, out);
            out.setFormType(InventoryMovementConstant.OUT_TYPE);
            out.setQuantity(added.negate());
            out.setRackId(it.getSourceRackId());
            out.setWarehouseId(it.getSourceWarehouseId());
            out.setAreaId(it.getSourceAreaId());
            outList.add(out);
        });
        if (outList.size() > 0) {
            inventoryHistoryService.batchInsert(outList);
            inventoryService.batchUpdate1(outList);
        }
        if (inList.size() > 0) {
            inventoryHistoryService.batchInsert(inList);
            inventoryService.batchUpdate1(inList);
        }
        // 2.1 先删除details 再重新保存
        inventoryMovementDetailMapper.delete(qw);
        saveDetails(order.getId(), order.getDetails());

        // 2.2 更新移库单
        //判断移库单的整体状态
        Set<Integer> statusList = order.getDetails().stream().map(it -> it.getMoveStatus()).collect(Collectors.toSet());
        if (statusList.size() == 1) {
            order.setStatus(statusList.iterator().next());
        } else if (statusList.contains(InventoryMovementConstant.PART_IN)) {
            order.setStatus(InventoryMovementConstant.PART_IN);
        }
        res = inventoryMovementMapper.updateById(order);
        return res;
    }

    private void saveDetails(Long orderId, List<InventoryMovementDetailVO> details) {
        if (!CollUtil.isEmpty(details)) {
            details.forEach(it -> it.setInventoryMovementId(orderId));
            List<InventoryMovementDetail> inventoryMovementDetails = detailConvert.vos2dos(details);
            inventoryMovementDetailMapper.batchInsert(inventoryMovementDetails);
        }
    }
}
