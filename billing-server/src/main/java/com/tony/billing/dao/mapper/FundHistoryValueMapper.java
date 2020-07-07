package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.FundHistoryValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
@Repository
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
     *
     * @param fundCodes
     * @return
     */
    List<FundHistoryValue> getFundHistoriesByFundCodes(@Param("fundCodes") List<String> fundCodes, @Param("assessmentDate") String assessmentDate);

    /**
     * 获取最新估值信息
     *
     * @param fundCodes
     * @param assessmentDate
     * @return
     */
    List<FundHistoryValue> getLatestFundHistoryValueByFundCodes(@Param("fundCodes") List<String> fundCodes, @Param("assessmentDate") String assessmentDate);

    /**
     * 获取某个基金指定日期的最新估值
     * @param fundCode
     * @param assessmentDate
     * @return
     */
    FundHistoryValue getFundLatestValue(@Param("fundCode") String fundCode, @Param("assessmentDate") String assessmentDate);
}
