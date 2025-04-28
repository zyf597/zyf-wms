package com.zyf.domain.query;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 波次 查询 对象
 *
 * @author zyf
 */
@ApiModel(description="波次 查询 对象")
@Data
public class WaveQuery {
    @ApiModelProperty("波次号 精确匹配")
    private String waveNo;

    @ApiModelProperty("状态 精确匹配")
    private String status;

    private Integer type;

}
