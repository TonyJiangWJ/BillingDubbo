package com.tony.billing.service.api;

import com.tony.billing.response.fund.DailyFundChangedResponse;
import com.tony.billing.response.fund.DailyFundHistoryValueResponse;

/**
 * @author jiangwenjie 2020/6/28
 */
public interface FundHistoryValueService {
    /**
     * 获取当前在库基金，并获取最新估值
     * @return
     */
    int updateFundHistoryValues();

    /**
     * 获取用户基金指定日期的估值变化
     *
     * @param assessmentDate 估算日期
     * @return
     */
    DailyFundHistoryValueResponse getFundHistoryValuesByAssessmentDate(String assessmentDate);

    /**
     * 获取所有基金的估值变化明细
     *
     * @param assessmentDate 估算日期
     * @return
     */
    DailyFundChangedResponse getFundChangedInfosByAssessmentDate(String assessmentDate);
}
