package com.zyf.domain.vo;

import com.zyf.common.annotation.Excel;
import com.zyf.common.core.domain.BaseAudit;
import lombok.Data;
/**
 * 波次 数据视图对象
 *
 * @author zyf
 */
@Data
public class WaveVO extends BaseAudit {
   /** ID */
    private Long id;
   /** 波次号 */
    @Excel(name = "波次号")
    private String waveNo;
   /** 状态 */
    @Excel(name = "状态")
    private String status;
   /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
