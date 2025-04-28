package com.zyf.controller;

import com.zyf.convert.WaveConvert;
import com.zyf.domain.entity.Wave;
import com.zyf.domain.query.WaveQuery;
import com.zyf.domain.vo.WaveVO;
import com.zyf.domain.form.OrderWaveFrom;
import com.zyf.domain.form.OrderWaveReceiptFrom;
import com.zyf.service.WaveForReceiptService;
import com.zyf.service.WaveService;
import com.zyf.common.annotation.Log;
import com.zyf.common.core.controller.BaseController;
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
 * 波次Controller
 *
 * @author zyf
 * @date 2023-08-16
 */
@Api(description = "波次接口列表")
@RestController
@RequestMapping("/wms/wave")
public class WaveController extends BaseController {
    @Autowired
    private WaveService service;
    @Autowired
    private WaveConvert convert;
    @Autowired
    private WaveForReceiptService waveForReceiptService;

    @ApiOperation("查询波次列表")
    @PreAuthorize("@ss.hasPermi('wms:wave:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<Wave>> list(@RequestBody WaveQuery query, Pageable page) {
        List<Wave> list = service.selectList(query, page);
        return ResponseEntity.ok(new PageImpl<>(list, page, ((com.github.pagehelper.Page) list).getTotal()));
    }

    @ApiOperation("导出波次列表")
    @PreAuthorize("@ss.hasPermi('wms:wave:export')")
    @Log(title = "波次", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResponseEntity<String> export(WaveQuery query) {
        List<Wave> list = service.selectList(query, null);
        ExcelUtil<WaveVO> util = new ExcelUtil<>(WaveVO.class);
        return ResponseEntity.ok(util.writeExcel(convert.dos2vos(list), "波次数据"));
    }

    @ApiOperation("获取出库波次详细信息")
    @PreAuthorize("@ss.hasPermi('wms:wave:query')")
    @GetMapping(value = "/shipment/{id}")
    public ResponseEntity<OrderWaveFrom> getInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getShipmentOrders(id));
    }

    @ApiOperation("获取入库波次详细信息")
    @PreAuthorize("@ss.hasPermi('wms:wave:query')")
    @GetMapping(value = "/receipt/{id}")
    public ResponseEntity<OrderWaveReceiptFrom> getReceiptInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(waveForReceiptService.getReceiptOrders(id));
    }

    @ApiOperation("新增出库波次")
    @PreAuthorize("@ss.hasPermi('wms:wave:add')")
    @Log(title = "波次", businessType = BusinessType.INSERT)
    @PostMapping("/shipment/add")
    public ResponseEntity<Integer> add(@RequestBody Wave wave) {
        return ResponseEntity.ok(service.creatWave(wave));
    }

    @ApiOperation("新增入库波次")
    @PreAuthorize("@ss.hasPermi('wms:wave:add')")
    @Log(title = "波次", businessType = BusinessType.INSERT)
    @PostMapping("/receipt/add")
    public ResponseEntity<Integer> addForReceipt(@RequestBody Wave wave) {
        return ResponseEntity.ok(waveForReceiptService.creatWaveForReceipt(wave));
    }

    @ApiOperation("波次单为出库分配仓库")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/shipment/allocated")
    public ResponseEntity<OrderWaveFrom> allocatedInventory(Long id,Integer type) {

        return ResponseEntity.ok(service.allocatedInventory(id,type));
    }

    @ApiOperation("波次单为入库分配仓库")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/receipt/allocated")
    public ResponseEntity<OrderWaveReceiptFrom> allocatedInventoryForReceipt(Long id,Integer type) {

        return ResponseEntity.ok(waveForReceiptService.allocatedInventoryForReceipt(id,type));
    }

    @ApiOperation("应用波次作业为出库单")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/shipment/confirmWave")
    public ResponseEntity<Integer> confirmWave(@RequestBody OrderWaveFrom order) {
        return ResponseEntity.ok(service.confirmWave(order));
    }

    @ApiOperation("应用波次作业为入库单")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/receipt/confirmWave")
    public ResponseEntity<Integer> confirmWaveForReceipt(@RequestBody OrderWaveReceiptFrom order) {
        return ResponseEntity.ok(waveForReceiptService.confirmWaveForReceipt(order));
    }

    @ApiOperation("取消出库波次作业")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/shipment/cancelAllocatedInventory/{id}")
    public ResponseEntity<Integer> cancelAllocatedInventory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.cancelAllocatedInventory(id));
    }

    @ApiOperation("取消入库波次作业")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次单", businessType = BusinessType.UPDATE)
    @PostMapping("/receipt/cancelAllocatedInventory/{id}")
    public ResponseEntity<Integer> cancelAllocatedInventoryForReceipt(@PathVariable("id") Long id) {
        return ResponseEntity.ok(waveForReceiptService.cancelAllocatedInventoryForReceipt(id));
    }

    @ApiOperation("修改波次")
    @PreAuthorize("@ss.hasPermi('wms:wave:edit')")
    @Log(title = "波次", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody Wave wave) {
        return ResponseEntity.ok(service.update(wave));
    }

    @ApiOperation("删除出库波次")
    @PreAuthorize("@ss.hasPermi('wms:wave:remove')")
    @Log(title = "波次", businessType = BusinessType.DELETE)
    @DeleteMapping("/shipment/{ids}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] ids) {
        return ResponseEntity.ok(service.deleteByIds(ids));
    }

    @ApiOperation("删除入库波次")
    @PreAuthorize("@ss.hasPermi('wms:wave:remove')")
    @Log(title = "波次", businessType = BusinessType.DELETE)
    @DeleteMapping("/receipt/{ids}")
    public ResponseEntity<Integer> removeForReceipt(@PathVariable Long[] ids) {
        return ResponseEntity.ok(waveForReceiptService.deleteByIdsForReceipt(ids));
    }

}
