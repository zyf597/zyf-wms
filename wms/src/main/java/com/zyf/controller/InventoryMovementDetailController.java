package com.zyf.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyf.common.annotation.Log;
import com.zyf.common.core.controller.BaseController;
import com.zyf.common.enums.BusinessType;
import com.zyf.convert.InventoryMovementDetailConvert;
import com.zyf.domain.entity.InventoryMovementDetail;
import com.zyf.domain.query.InventoryMovementDetailQuery;
import com.zyf.service.InventoryMovementDetailService;
import com.zyf.domain.vo.InventoryMovementDetailVO;
import com.zyf.common.utils.poi.ExcelUtil;
/**
 * 库存移动详情Controller
 *
 * @author zyf
 * @date 2022-11-02
 */
@Api(description ="库存移动详情接口列表")
@RestController
@RequestMapping("/wms/inventoryMovementDetail")
public class InventoryMovementDetailController extends BaseController {
    @Autowired
    private InventoryMovementDetailService service;
    @Autowired
    private InventoryMovementDetailConvert convert;

    @ApiOperation("查询库存移动详情列表")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<InventoryMovementDetail>> list(@RequestBody InventoryMovementDetailQuery query, Pageable page) {
        List<InventoryMovementDetail> list = service.selectList(query, page);
        return ResponseEntity.ok(new PageImpl<>(list, page, ((com.github.pagehelper.Page)list).getTotal()));
    }

    @ApiOperation("导出库存移动详情列表")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:export')")
    @Log(title = "库存移动详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResponseEntity<String> export(InventoryMovementDetailQuery query) {
        List<InventoryMovementDetail> list = service.selectList(query, null);
        ExcelUtil<InventoryMovementDetailVO> util = new ExcelUtil<>(InventoryMovementDetailVO.class);
        return ResponseEntity.ok(util.writeExcel(convert.dos2vos(list), "库存移动详情数据"));
    }

    @ApiOperation("获取库存移动详情详细信息")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:query')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InventoryMovementDetail> getInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.selectById(id));
    }

    @ApiOperation("新增库存移动详情")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:add')")
    @Log(title = "库存移动详情", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody InventoryMovementDetail inventoryMovementDetail) {
        return ResponseEntity.ok(service.insert(inventoryMovementDetail));
    }

    @ApiOperation("修改库存移动详情")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:edit')")
    @Log(title = "库存移动详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody InventoryMovementDetail inventoryMovementDetail) {
        return ResponseEntity.ok(service.update(inventoryMovementDetail));
    }

    @ApiOperation("删除库存移动详情")
    @PreAuthorize("@ss.hasPermi('wms:inventoryMovementDetail:remove')")
    @Log(title = "库存移动详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] ids) {
        return ResponseEntity.ok(service.deleteByIds(ids));
    }
}
