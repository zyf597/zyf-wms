package com.zyf.domain.vo;

import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
/**
 * 货区 数据视图对象
 *
 * @author zyf
 */
@Data
public class AreaVO extends BaseAudit {
   /** ID */
    private Long id;
   /** 货区编号 */
    @Excel(name = "货区编号")
    private String areaNo;
   /** 货区名称 */
    @Excel(name = "货区名称")
    private String areaName;
   /** 所属仓库ID */
    @Excel(name = "所属仓库ID")
    private Long warehouseId;
   /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
