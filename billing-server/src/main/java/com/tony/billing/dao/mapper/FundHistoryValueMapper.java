package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.FundHistoryValue;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
public interface FundHistoryValueMapper extends AbstractMapper<FundHistoryValue> {
    /**
     * 判断是否已存在估值信息
     *
     * @param fundCode
     * @param assessmentTime
     * @return
     */
    int checkIsHistoryValueExists(@Param("fundCode") String fundCode, @Param("assessmentTime") Date assessmentTime);

    /**
     * 获取历史列表
     * @param fundCodes
     * @return
     */
    List<FundHistoryValue> getFundHistoriesByFundCodes(@Param("fundCodes") List<String> fundCodes, @Param("confirmDate") String confirmDate);
}
