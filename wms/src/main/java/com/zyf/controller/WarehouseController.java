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
import com.zyf.convert.WarehouseConvert;
import com.zyf.domain.entity.Warehouse;
import com.zyf.domain.query.WarehouseQuery;
import com.zyf.service.WarehouseService;
import com.zyf.domain.vo.WarehouseVO;
import com.zyf.common.utils.poi.ExcelUtil;
/**
 * 仓库Controller
 *
 * @author zyf
 * @date 2022-08-05
 */
@Api(description ="仓库接口列表")
@RestController
@RequestMapping("/wms/warehouse")
public class WarehouseController extends BaseController {
    @Autowired
    private WarehouseService service;
    @Autowired
    private WarehouseConvert convert;

    @ApiOperation("查询仓库列表")
    @PreAuthorize("@ss.hasPermi('wms:warehouse:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<Warehouse>> list(@RequestBody WarehouseQuery query, Pageable page) {
        List<Warehouse> list = service.selectList(query, page);
        return ResponseEntity.ok(new PageImpl<>(list, page, ((com.github.pagehelper.Page)list).getTotal()));
    }

    @ApiOperation("导出仓库列表")
    @PreAuthorize("@ss.hasPermi('wms:warehouse:export')")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResponseEntity<String> export(WarehouseQuery query) {
        List<Warehouse> list = service.selectList(query, null);
        ExcelUtil<WarehouseVO> util = new ExcelUtil<>(WarehouseVO.class);
        return ResponseEntity.ok(util.writeExcel(convert.dos2vos(list), "仓库数据"));
    }

    @ApiOperation("获取仓库详细信息")
    @PreAuthorize("@ss.hasPermi('wms:warehouse:query')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Warehouse> getInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.selectById(id));
    }

    @ApiOperation("新增仓库")
//    @PreAuthorize("@ss.hasPermi('wms:warehouse:add')")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(service.insert(warehouse));
    }

    @ApiOperation("修改仓库")
//    @PreAuthorize("@ss.hasPermi('wms:warehouse:edit')")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(service.update(warehouse));
    }

    @ApiOperation("删除仓库")
//    @PreAuthorize("@ss.hasPermi('wms:warehouse:remove')")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] ids) {
        return ResponseEntity.ok(service.deleteByIds(ids));
    }
}
