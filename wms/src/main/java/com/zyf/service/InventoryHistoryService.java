package com.zyf.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyf.convert.InventoryHistoryConvert;
import com.zyf.domain.entity.InventoryHistory;
import com.zyf.mapper.InventoryHistoryMapper;
import com.zyf.domain.query.InventoryHistoryQuery;
import com.zyf.domain.vo.InventoryHistoryVO;
import com.github.pagehelper.PageHelper;
import com.zyf.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 库存记录Service业务层处理
 *
 * @author zyf
 */
@Service
public class InventoryHistoryService {
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;
    @Autowired
    private InventoryHistoryConvert inventoryHistoryConvert;
    @Autowired
    private InventoryService inventoryService;

    /**
     * 查询库存记录
     *
     * @param id 库存记录主键
     * @return 库存记录
     */
    public InventoryHistory selectById(Long id) {
        return inventoryHistoryMapper.selectById(id);
    }

    /**
     * 查询库存记录列表
     *
     * @param query 查询条件
     * @return 库存记录
     */
    public List<InventoryHistoryVO> selectList(InventoryHistoryQuery query) {
        List<InventoryHistory> list = queryInventoryHistories(query);
        List<InventoryHistoryVO> res = inventoryHistoryConvert.dos2vos(list);
        inventoryService.injectAreaAndItemInfo(res);
        inventoryService.injectDictDataLabel(res);
        return res;
    }

    private List<InventoryHistory> queryInventoryHistories(InventoryHistoryQuery query) {
        QueryWrapper<InventoryHistory> qw = new QueryWrapper<>();
        qw.eq("del_flag", 0);
        Long formId = query.getFormId();
        if (formId != null) {
            qw.eq("form_id", formId);
        }
        Integer formType = query.getFormType();
        if (formType != null) {
            qw.eq("form_type", formType);
        }
        Long itemId = query.getItemId();
        if (itemId != null) {
            qw.eq("item_id", itemId);
        }
        if (query.getWarehouseId() != null) {
            qw.eq("warehouse_id", query.getWarehouseId());
        }
        if (query.getAreaId() != null) {
            qw.eq("area_id", query.getAreaId());
        }
        Long rackId = query.getRackId();
        if (rackId != null) {
            qw.eq("rack_id", rackId);
        }
        BigDecimal quantity = query.getQuantity();
        if (quantity != null) {
            qw.eq("quantity", quantity);
        }
        List<InventoryHistory> list = inventoryHistoryMapper.selectList(qw);
        return list;
    }

    public Page<InventoryHistoryVO> selectList(InventoryHistoryQuery query, Pageable page) {
        PageHelper.startPage(page.getPageNumber() + 1, page.getPageSize(), "create_time desc");
        List<InventoryHistory> list = queryInventoryHistories(query);
        List<InventoryHistoryVO> res = inventoryHistoryConvert.dos2vos(list);
        inventoryService.injectAreaAndItemInfo(res);
        return new PageImpl<>(res, page, ((com.github.pagehelper.Page) list).getTotal());
    }

    /**
     * 新增库存记录
     *
     * @param inventoryHistory 库存记录
     * @return 结果
     */
    public int insert(InventoryHistory inventoryHistory) {
        inventoryHistory.setDelFlag(0);
        inventoryHistory.setCreateTime(LocalDateTime.now());
        return inventoryHistoryMapper.insert(inventoryHistory);
    }

    /**
     * 修改库存记录
     *
     * @param inventoryHistory 库存记录
     * @return 结果
     */
    public int update(InventoryHistory inventoryHistory) {
        return inventoryHistoryMapper.updateById(inventoryHistory);
    }

    /**
     * 批量删除库存记录
     *
     * @param ids 需要删除的库存记录主键
     * @return 结果
     */
    public int deleteByIds(Long[] ids) {
        return inventoryHistoryMapper.updateDelFlagByIds(ids);
    }

    /**
     * 删除库存记录信息
     *
     * @param id 库存记录主键
     * @return 结果
     */
    public int deleteById(Long id) {
        Long[] ids = {id};
        return inventoryHistoryMapper.updateDelFlagByIds(ids);
    }

    /**
     * 删除库存记录信息
     *
     * @param formId   单据id
     * @param formType 单据类型
     * @return 结果
     */
    public int deleteByForm(Long formId, Integer... formType) {
        LambdaQueryWrapper<InventoryHistory> qw = new LambdaQueryWrapper<InventoryHistory>()
                .eq(InventoryHistory::getFormId, formId);
        if (formType.length > 0) {
            List<Integer> list = Arrays.asList(formType);
            qw.in(InventoryHistory::getFormType, list);
        }
        return inventoryHistoryMapper.delete(qw);
    }

    /**
     * 查询库存记录信息
     *
     * @param formId   单据id
     * @param formType 单据类型
     * @return 结果
     */
    public List<InventoryHistory> selectByForm(Long formId, Integer... formType) {
        LambdaQueryWrapper<InventoryHistory> qw = new LambdaQueryWrapper<InventoryHistory>()
                .eq(InventoryHistory::getFormId, formId);
        if (formType.length > 0) {
            List<Integer> list = Arrays.asList(formType);
            qw.in(InventoryHistory::getFormType, list);
        }
        return inventoryHistoryMapper.selectList(qw);
    }

    /**
     * 批量新增库存记录
     *
     * @param list 库存记录列表
     * @return 结果
     */
    public int batchInsert(List<InventoryHistory> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        int re = 0;
        for (int s = 0; s < list.size(); s += CommonConstant.BATCH_SIZE) {
            re += inventoryHistoryMapper.batchInsert(list.subList(s, Math.min(s + CommonConstant.BATCH_SIZE, list.size())));
        }
        return re;
    }

}
