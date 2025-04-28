package com.zyf.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.zyf.domain.entity.CustomerTransaction;

/**
 * 客户账户流水Mapper接口
 *
 * @author zyf
 */
public interface CustomerTransactionMapper extends BaseMapper<CustomerTransaction> {
    /**
     * 查询客户账户流水列表
     *
     * @param customerTransaction 客户账户流水
     * @return 客户账户流水集合
     */
    List<CustomerTransaction> selectByEntity(CustomerTransaction customerTransaction);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
