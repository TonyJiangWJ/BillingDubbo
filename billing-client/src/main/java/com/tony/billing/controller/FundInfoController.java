package com.tony.billing.controller;

import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.request.fund.FundAddRequest;
import com.tony.billing.request.fund.FundDeleteRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.util.ResponseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author jiangwenjie 2020/6/29
 */
@RestController
@RequestMapping("/bootDemo")
public class FundInfoController extends BaseController {

    @Reference
    private FundInfoService fundInfoService;

    @RequestMapping(value = "/fund/info/put", method = RequestMethod.POST)
    public BaseResponse addFundInfo(@ModelAttribute("request") @Validated FundAddRequest request) {
        FundInfo fundInfo = new FundInfo();
        fundInfo.setUserId(request.getUserId());
        fundInfo.setFundName(request.getFundName());
        fundInfo.setFundCode(request.getFundCode());
        fundInfo.setPurchaseAmount(new BigDecimal(request.getPurchaseAmount()));
        fundInfo.setPurchaseValue(new BigDecimal(request.getPurchaseValue()));
        fundInfo.setPurchaseCost(new BigDecimal(request.getPurchaseCost()));
        fundInfo.setPurchaseFee(new BigDecimal(request.getPurchaseFee()));
        fundInfo.setInStore(EnumYesOrNo.YES.val());
        fundInfo.setConfirmDate(request.getPurchaseConfirmedDate());
        fundInfo.setPurchaseDate(request.getPurchaseDate());

        if (fundInfoService.insert(fundInfo) > 0) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    @RequestMapping(value = "/fund/info/delete", method = RequestMethod.POST)
    public BaseResponse deleteFundInfo(@ModelAttribute("request") @Validated FundDeleteRequest request) {

        if (fundInfoService.deleteById(request.getId())) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }

    }
}
