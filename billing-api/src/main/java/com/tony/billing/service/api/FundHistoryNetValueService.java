package com.tony.billing.service.api;


import com.tony.billing.entity.FundHistoryNetValue;
import com.tony.billing.response.fund.FundHistoryNetValueResponse;
import com.tony.billing.service.api.base.AbstractService;

/**
 * @author jiangwj20966 2020/07/13
 */
public interface FundHistoryNetValueService extends AbstractService<FundHistoryNetValue> {

    /**
     * 自动更新库内基金的历史净值数据
     * @return 更新的基金数量
     */
    int updateHistoryNetValues();

    /**
     * 更新指定基金的历史净值数据
     *
     * @param fundCode
     */
    void updateFundHistoryNetValue(String fundCode);

    /**
     * 获取指定基金的历史净值数据 近30天
     * @param fundCode
     * @param dateAfter
     * @return
     */
    FundHistoryNetValueResponse getHistoryNetValuesByFundCode(String fundCode, String dateAfter);
}
