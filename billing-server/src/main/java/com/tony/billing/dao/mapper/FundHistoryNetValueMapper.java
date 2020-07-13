package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.FundHistoryNetValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangwj20966 2020/07/13
 */
@Repository
public interface FundHistoryNetValueMapper extends AbstractMapper<FundHistoryNetValue> {

    /**
     * 批量新增
     * @param fundNetValueList
     * @return
     */
    int batchInsert(@Param("fundNetValueList") List<FundHistoryNetValue> fundNetValueList);
}
