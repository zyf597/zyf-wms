package com.zyf.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.Rack;

/**
 * 货架Mapper接口
 *
 * @author zyf
 */
public interface RackMapper extends BaseMapper<Rack> {
    /**
     * 查询货架列表
     *
     * @param rack 货架
     * @return 货架集合
     */
    List<Rack> selectByEntity(Rack rack);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
