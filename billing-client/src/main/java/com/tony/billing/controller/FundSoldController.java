package com.tony.billing.controller;

import com.tony.billing.entity.FundSoldInfo;
import com.tony.billing.entity.query.FundPreSaleInfoQuery;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.request.fund.FundPreSalePageRequest;
import com.tony.billing.request.fund.FundPreSalePortionRequest;
import com.tony.billing.request.fund.FundPreSaleRequest;
import com.tony.billing.request.fund.FundSoldRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.response.fund.FundPreSalePageResponse;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.api.FundPreSaleInfoService;
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
public class FundSoldController extends BaseController {
    @Reference
    private FundSoldInfoService fundSoldInfoService;
    @Reference
    private FundInfoService fundInfoService;
    @Reference
    private FundPreSaleInfoService fundPreSaleInfoService;

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

    /**
     * 列表，预出售列表
     *  -- 查看详情 -- 列出关联基金
     *  -- 删除撤销 -- 同步恢复关联的基金数据
     *  -- 标记为已出售，将数据转换为已出售 -- 同步转换关联的基金数据
     * 已出售列表
     *  -- 查看详情
     */
    @RequestMapping(value = "/fund/pre/sale/page")
    public FundPreSalePageResponse queryPreSaleInfo(@ModelAttribute("query") @Validated FundPreSalePageRequest request) {
        FundPreSaleInfoQuery query = new FundPreSaleInfoQuery();
        query.setFundCode(request.getFundCode());
        query.setFundName(request.getFundName());
        query.setUserId(request.getUserId());
        query.setPageNo(request.getPageNo());
        query.setPageSize(request.getPageSize());
        query = fundPreSaleInfoService.page(query);
        if (query != null) {
            return new FundPreSalePageResponse(query);
        }
        return ResponseUtil.dataNotExisting(new FundPreSalePageResponse());
    }
}
