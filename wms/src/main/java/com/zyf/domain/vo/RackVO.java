package com.zyf.domain.vo;

import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
/**
 * 货架 数据视图对象
 *
 * @author zyf
 */
@Data
public class RackVO extends BaseAudit {
   /** ID */
    private Long id;
   /** 货架编号 */
    @Excel(name = "货架编号")
    private String rackNo;
   /** 货架名称 */
    @Excel(name = "货架名称")
    private String rackName;
   /** 所属货区 */
    @Excel(name = "所属货区")
    private Long areaId;
   /** 所属仓库 */
    @Excel(name = "所属仓库")
    private Long warehouseId;
   /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
