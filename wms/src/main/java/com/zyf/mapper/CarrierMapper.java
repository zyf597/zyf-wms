package com.zyf.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.Carrier;

/**
 * 承运商Mapper接口
 *
 * @author zyf
 */
public interface CarrierMapper extends BaseMapper<Carrier> {
    /**
     * 查询承运商列表
     *
     * @param carrier 承运商
     * @return 承运商集合
     */
    List<Carrier> selectByEntity(Carrier carrier);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
