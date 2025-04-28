package com.zyf.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.Warehouse;

/**
 * 仓库Mapper接口
 *
 * @author zyf
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {
    /**
     * 查询仓库列表
     *
     * @param warehouse 仓库
     * @return 仓库集合
     */
    List<Warehouse> selectByEntity(Warehouse warehouse);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
