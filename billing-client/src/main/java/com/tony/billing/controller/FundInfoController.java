package com.tony.billing.controller;

import com.alibaba.fastjson.JSONObject;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.model.FundExistenceCheck;
import com.tony.billing.request.fund.FundAddRequest;
import com.tony.billing.request.fund.FundBatchAddRequest;
import com.tony.billing.request.fund.FundDeleteRequest;
import com.tony.billing.request.fund.FundEnhanceRequest;
import com.tony.billing.request.fund.FundInfoQueryRequest;
import com.tony.billing.request.fund.FundUpdateRequest;
import com.tony.billing.request.fund.FundsExistenceCheckRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.response.fund.FundInfoQueryResponse;
import com.tony.billing.response.fund.FundsExistenceCheckResponse;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.util.RedisUtils;
import com.tony.billing.util.ResponseUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangwenjie 2020/6/29
 */
@RestController
@RequestMapping("/bootDemo")
public class FundInfoController extends BaseController {

    @Reference
    private FundInfoService fundInfoService;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${fund.value.query.url:http://fundgz.1234567.com.cn/js/%s.js}")
    private String fundValueQueryUrl;

    private final String FUND_INFO_HASH_KEY = "fund_info_hash";
    private final String FUND_INFO_KEY_PREFIX = "fund_code_";

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
        fundInfo.setConfirmDate(request.getPurchaseConfirmDate());
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

    @RequestMapping(value = "/fund/info/query", method = RequestMethod.POST)
    public FundInfoQueryResponse queryFundInfo(@ModelAttribute("request") @Validated FundInfoQueryRequest request) {
        FundInfoQueryResponse response = ResponseUtil.success(new FundInfoQueryResponse());
        response.setFundCode(request.getFundCode());
        if (StringUtils.isEmpty(request.getPurchaseDate())) {
            Optional<String> fundName = redisUtils.hGet(FUND_INFO_HASH_KEY, FUND_INFO_KEY_PREFIX + request.getFundCode(), String.class);
            if (fundName.isPresent()) {
                response.setFundName(fundName.get());
                return response;
            }

            String queryUrl = String.format(fundValueQueryUrl, request.getFundCode());
            OkHttpClient client = new OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build();
            Request req = new Request.Builder().url(queryUrl).build();

            try (
                    Response res = client.newCall(req).execute()
            ) {
                if (res.isSuccessful() && res.body() != null) {
                    String responseBodyStr = res.body().string();
                    responseBodyStr = responseBodyStr.replaceAll("jsonpgz\\(", "").replaceAll("\\);", "");
                    logger.debug("responseBody: {}", responseBodyStr);
                    JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);

                    response.setFundConfirmedDate(jsonObject.getString("jzrq"));
                    response.setFundConfirmedValue(jsonObject.getString("dwjz"));
                    response.setFundName(jsonObject.getString("name"));

                    if (StringUtils.isNotEmpty(response.getFundName())) {
                        redisUtils.hSet(FUND_INFO_HASH_KEY, FUND_INFO_KEY_PREFIX + request.getFundCode(), response.getFundName());
                        return response;
                    }
                }
            } catch (IOException e) {
                logger.error("获取基金:" + request.getFundCode() + " 估值信息失败", e);
            }
        }
        return ResponseUtil.dataNotExisting(response);
    }

    @RequestMapping(value = "/fund/info/update", method = RequestMethod.POST)
    public BaseResponse updateFundInfo(@ModelAttribute("request") @Validated FundUpdateRequest request) {
        FundInfo fundInfo = new FundInfo();
        BeanUtils.copyProperties(request, fundInfo);
        fundInfo.setConfirmDate(request.getPurchaseConfirmDate());
        if (fundInfoService.update(fundInfo)) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    @RequestMapping(value = "/fund/check/all/status", method = RequestMethod.POST)
    public FundsExistenceCheckResponse checkAllFundsStatus(@ModelAttribute("request") @Validated FundsExistenceCheckRequest request) {
        List<FundExistenceCheck> existsList = fundInfoService.checkFundsExistence(request.getFundCheckList());
        FundsExistenceCheckResponse response = ResponseUtil.success(new FundsExistenceCheckResponse());
        response.setExistsList(existsList);
        return response;
    }

    @RequestMapping(value = "/fund/batch/add", method = RequestMethod.POST)
    public BaseResponse batchAddFunds(@ModelAttribute("request") @Validated FundBatchAddRequest request) {
        if (fundInfoService.batchAddFunds(request.getFundInfoList())) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    @PostMapping("/fund/info/enhance")
    public BaseResponse enhanceFund(@ModelAttribute("request") @Validated FundEnhanceRequest request) {
        if (fundInfoService.enhanceFund(request)) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }
}
