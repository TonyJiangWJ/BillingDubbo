package com.tony.billing.controller;

import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.request.fund.FundPreSoldRequest;
import com.tony.billing.request.fund.FundSoldRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.api.FundSoldInfoService;
import com.tony.billing.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangwenjie 2020/7/6
 */
@Slf4j
@RestController
@RequestMapping("/bootDemo")
public class FunSoldController extends BaseController {
    @Reference
    private FundSoldInfoService fundSoldInfoService;
    @Reference
    private FundInfoService fundInfoService;

    @RequestMapping(value = "/fund/sold/put", method = RequestMethod.POST)
    public BaseResponse addSoldInfo(@ModelAttribute("request") @Validated FundSoldRequest request) {
        return ResponseUtil.success();
    }


    @RequestMapping(value = "/fund/pre/mark/as/sold", method = RequestMethod.POST)
    public BaseResponse markFundAsSold(@ModelAttribute("request") @Validated FundPreSoldRequest request) {
        BaseResponse response = ResponseUtil.success();
        try {
            if (!fundInfoService.preMarkFundsAsSold(request.getFundIds(), request.getFundSoldFeeRate(), request.getAssessmentDate())) {
                response = ResponseUtil.error();
            }
        } catch (BaseBusinessException e) {
            response = ResponseUtil.error();
            response.setMsg(e.getMessage());
        }
        return response;
    }
}
