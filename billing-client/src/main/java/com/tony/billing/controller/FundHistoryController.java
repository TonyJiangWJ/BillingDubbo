package com.tony.billing.controller;

import com.tony.billing.response.fund.DailyFundChangedResponse;
import com.tony.billing.response.fund.DailyFundHistoryValueResponse;
import com.tony.billing.service.api.FundHistoryValueService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基金估值变化
 *
 * @author jiangwenjie 2020/6/28
 */
@RestController
@RequestMapping("/bootDemo")
public class FundHistoryController {


    @Reference
    private FundHistoryValueService fundHistoryValueService;

    /**
     * 基金指定日期估算净值变化曲线
     *
     * @param assessmentDate
     * @return
     */
    @RequestMapping("/fund/history/value/get")
    public DailyFundHistoryValueResponse getDailyFundHistoryResult(@RequestParam("assessmentDate") String assessmentDate) {
        return fundHistoryValueService.getFundHistoryValuesByAssessmentDate(assessmentDate);
    }

    /**
     * 基金指定日期估算净值明细，附带持有的各个基金详细信息
     *
     * @param assessmentDate
     * @return
     */
    @RequestMapping("/fund/changed/get")
    public DailyFundChangedResponse getDailyFundChangedResult(@RequestParam("assessmentDate") String assessmentDate) {
        return fundHistoryValueService.getFundChangedInfosByAssessmentDate(assessmentDate);
    }
}
