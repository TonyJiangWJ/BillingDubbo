package com.tony.billing.controller;

import com.tony.billing.response.fund.DailyFundHistoryValueResponse;
import com.tony.billing.service.api.FundHistoryValueService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangwenjie 2020/6/28
 */
@RestController
@RequestMapping("/bootDemo")
public class FundHistoryController {


    @Reference
    private FundHistoryValueService fundHistoryValueService;

    @RequestMapping("/fund/history/value/get")
    public DailyFundHistoryValueResponse getDailyFundHistoryResult(@RequestParam("confirmDate") String confirmDate) {
        return fundHistoryValueService.getFundHistoryValuesByConfirmDate(confirmDate);
    }
}