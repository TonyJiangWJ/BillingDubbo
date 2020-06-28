package com.tony.billing.service.api;

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
     * @param confirmDate 确认时间
     * @return
     */
    DailyFundHistoryValueResponse getFundHistoryValuesByConfirmDate(String confirmDate);
}
