package com.zyf.controller;

import com.zyf.convert.ItemTypeConvert;
import com.zyf.domain.entity.ItemType;
import com.zyf.domain.query.ItemTypeQuery;
import com.zyf.domain.vo.ItemTypeVO;
import com.zyf.service.ItemTypeService;
import com.zyf.common.annotation.Log;
import com.zyf.common.core.controller.BaseController;
import com.zyf.common.core.domain.AjaxResult;
import com.zyf.common.enums.BusinessType;
import com.zyf.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料类型表Controller
 *
 * @author zyf
 * @date 2023-04-03
 */
@Api(description = "物料类型表接口列表")
@RestController
@RequestMapping("/wms/itemType")
public class ItemTypeController extends BaseController {
    @Autowired
    private ItemTypeService service;
    @Autowired
    private ItemTypeConvert convert;

    @ApiOperation("查询物料类型表列表")
    @PreAuthorize("@ss.hasPermi('wms:itemType:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<ItemType>> list(@RequestBody ItemTypeQuery query, Pageable page) {
        List<ItemType> list = service.selectList(query, page);
        return ResponseEntity.ok(new PageImpl<>(list, page, ((com.github.pagehelper.Page) list).getTotal()));
    }

    /**
     * 查询物料类型表列表
     */
    @PreAuthorize("@ss.hasPermi('wms:itemType:list')")
    @GetMapping("/noPageList")
    public AjaxResult list(ItemTypeQuery query) {
        List<ItemType> depts = service.selectList(query, null);
        return AjaxResult.success(depts);
    }

    @ApiOperation("导出物料类型表列表")
    @PreAuthorize("@ss.hasPermi('wms:itemType:export')")
    @Log(title = "物料类型表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResponseEntity<String> export(ItemTypeQuery query) {
        List<ItemType> list = service.selectList(query, null);
        ExcelUtil<ItemTypeVO> util = new ExcelUtil<>(ItemTypeVO.class);
        return ResponseEntity.ok(util.writeExcel(convert.dos2vos(list), "物料类型表数据"));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(ItemTypeQuery query) {
        List<ItemType> itemTypes = service.selectList(query, null);
        return AjaxResult.success(service.buildItemTypeTreeSelect(itemTypes));
    }

    @ApiOperation("获取物料类型表详细信息")
    @PreAuthorize("@ss.hasPermi('wms:itemType:query')")
    @GetMapping(value = "/{itemTypeId}")
    public ResponseEntity<ItemType> getInfo(@PathVariable("itemTypeId") Long itemTypeId) {
        return ResponseEntity.ok(service.selectByItemTypeId(itemTypeId));
    }

    @ApiOperation("新增物料类型表")
    @PreAuthorize("@ss.hasRole('admin')")
//    @PreAuthorize("@ss.hasPermi('wms:itemType:add')")
    @Log(title = "物料类型表", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody ItemType itemType) {
        return ResponseEntity.ok(service.insert(itemType));
    }

    @ApiOperation("修改物料类型表")
    @PreAuthorize("@ss.hasRole('admin')")
//    @PreAuthorize("@ss.hasPermi('wms:itemType:edit')")
    @Log(title = "物料类型表", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody ItemType itemType) {
        return ResponseEntity.ok(service.update(itemType));
    }

    @ApiOperation("删除物料类型表")
    @PreAuthorize("@ss.hasRole('admin')")
//    @PreAuthorize("@ss.hasPermi('wms:itemType:remove')")
    @Log(title = "物料类型表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemTypeIds}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] itemTypeIds) {
        return ResponseEntity.ok(service.deleteByItemTypeIds(itemTypeIds));
    }
}
