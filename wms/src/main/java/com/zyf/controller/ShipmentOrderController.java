package com.zyf.controller;

import com.zyf.convert.ShipmentOrderConvert;
import com.zyf.domain.entity.ShipmentOrder;
import com.zyf.domain.query.ShipmentOrderQuery;
import com.zyf.domain.vo.ShipmentOrderVO;
import com.zyf.domain.form.ShipmentOrderFrom;
import com.zyf.service.ShipmentOrderService;
import com.zyf.common.annotation.Log;
import com.zyf.common.core.controller.BaseController;
import com.zyf.common.enums.BusinessType;
import com.zyf.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * 出库单Controller
 *
 * @author zyf
 * @date 2022-08-05
 */
@Api(description ="出库单接口列表")
@RestController
@RequestMapping("/wms/shipmentOrder")
public class ShipmentOrderController extends BaseController {
    @Autowired
    private ShipmentOrderService service;
    @Autowired
    private ShipmentOrderConvert convert;

    @ApiOperation("查询出库单列表")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<ShipmentOrderVO>> list(@RequestBody ShipmentOrderQuery query, Pageable page) {
        return ResponseEntity.ok(service.selectList(query, page));
    }

    @ApiOperation("导出出库单列表")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:export')")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResponseEntity<String> export(ShipmentOrderQuery query) {
        Page<ShipmentOrderVO> list = service.selectList(query, null);
        ExcelUtil<ShipmentOrderVO> util = new ExcelUtil<>(ShipmentOrderVO.class);
        return ResponseEntity.ok(util.writeExcel(list.getContent(), "出库单数据"));
    }

    @ApiOperation("获取出库单详细信息")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:query')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ShipmentOrderFrom> getInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.selectById(id));
    }

    @ApiOperation("新增出库单")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:add')")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody ShipmentOrder shipmentOrder) {
        return ResponseEntity.ok(service.insert(shipmentOrder));
    }

    @ApiOperation("修改出库单")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:edit')")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody ShipmentOrder shipmentOrder) {
        return ResponseEntity.ok(service.update(shipmentOrder));
    }

    @ApiOperation("删除出库单")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:remove')")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] ids) {
        return ResponseEntity.ok(service.deleteByIds(ids));
    }

    @ApiOperation("新增或更新出库单以及出库单明细")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:add')")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @PostMapping("add-or-update")
    public ResponseEntity<Integer> addOrUpdate(@RequestBody ShipmentOrderFrom order) {
        if(order.getId()==null){
            return ResponseEntity.ok(service.add(order));
        }else {
            return ResponseEntity.ok(service.update(order));
        }
    }
    @ApiOperation("单个订单分配仓库")
    @PreAuthorize("@ss.hasPermi('wms:shipmentOrder:edit')")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PostMapping("allocated")
    public ResponseEntity allocatedInventory(Long id,Integer type) {
        service.allocatedInventory(id,type);
        return ResponseEntity.ok().build();
    }
}
