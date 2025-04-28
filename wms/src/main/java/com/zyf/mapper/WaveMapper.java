package com.zyf.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.Wave;

/**
 * 波次Mapper接口
 *
 * @author zyf
 */
public interface WaveMapper extends BaseMapper<Wave> {
    /**
     * 查询波次列表
     *
     * @param wave 波次
     * @return 波次集合
     */
    List<Wave> selectByEntity(Wave wave);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
