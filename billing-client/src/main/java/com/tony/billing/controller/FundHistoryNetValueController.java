package com.tony.billing.controller;

import com.tony.billing.request.fund.FundInfoQueryRequest;
import com.tony.billing.response.fund.FundHistoryNetValueResponse;
import com.tony.billing.service.api.FundHistoryNetValueService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基金历史净值
 *
 * @author jiangwenjie 2020/9/3
 */
@RestController
@RequestMapping("/bootDemo")
public class FundHistoryNetValueController extends BaseController {

    @Reference
    private FundHistoryNetValueService fundHistoryNetValueService;

    @PostMapping("/fund/history/net/values")
    public FundHistoryNetValueResponse getFundHistoryNetValues(@ModelAttribute("request") @Validated FundInfoQueryRequest request) {
        return fundHistoryNetValueService.getHistoryNetValuesByFundCode(request.getFundCode(), request.getPurchaseDate());
    }
}
