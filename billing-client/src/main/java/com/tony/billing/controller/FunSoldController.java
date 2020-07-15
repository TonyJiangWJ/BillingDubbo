package com.tony.billing.controller;

import com.tony.billing.entity.FundSoldInfo;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.request.fund.FundPreSalePortionRequest;
import com.tony.billing.request.fund.FundPreSaleRequest;
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
        FundSoldInfo fundSoldInfo = new FundSoldInfo();
        fundSoldInfo.setUserId(request.getUserId());
        fundSoldInfo.setPurchaseCost(request.getPurchaseCost());
        fundSoldInfo.setPurchaseFee(request.getPurchaseFee());
        fundSoldInfo.setCostValue(request.getPurchaseCost().divide(request.getSoldAmount(), 4));
        fundSoldInfo.setFundCode(request.getFundCode());
        fundSoldInfo.setFundName(request.getFundName());
        fundSoldInfo.setSoldAmount(request.getSoldAmount());
        fundSoldInfo.setSoldConfirmDate(request.getSoldConfirmDate());
        fundSoldInfo.setSoldDate(request.getSoldDate());
        fundSoldInfo.setSoldFee(request.getSoldFee());
        fundSoldInfo.setSoldIncome(request.getSoldIncome());
        fundSoldInfo.setSoldValue(request.getSoldValue());
        if (fundSoldInfoService.insert(fundSoldInfo) > 0) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }


    @RequestMapping(value = "/fund/pre/mark/as/sold", method = RequestMethod.POST)
    public BaseResponse markFundAsSold(@ModelAttribute("request") @Validated FundPreSaleRequest request) {
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

    @RequestMapping(value = "/fund/pre/sale/portion", method = RequestMethod.POST)
    public BaseResponse preSalePortion(@ModelAttribute("request") @Validated FundPreSalePortionRequest request) {
        BaseResponse response = ResponseUtil.success();
        try {
            if (!fundInfoService.preSalePortion(request)) {
                response = ResponseUtil.error();
            }
        } catch (BaseBusinessException e) {
            response = ResponseUtil.error();
            response.setMsg(e.getMessage());
        }
        return response;
    }
}
