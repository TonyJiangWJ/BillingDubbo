package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundHistoryValue;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.entity.FundPreSaleInfo;
import com.tony.billing.entity.FundPreSaleRef;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.model.FundAddModel;
import com.tony.billing.model.FundExistenceCheck;
import com.tony.billing.request.fund.FundPreSalePortionRequest;
import com.tony.billing.service.api.FundHistoryValueService;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.api.FundPreSaleInfoService;
import com.tony.billing.service.api.FundPreSaleRefService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author jiangwenjie 2020/6/28
 */
@Service
public class FundInfoServiceImpl extends AbstractServiceImpl<FundInfo, FundInfoMapper> implements FundInfoService {

    @Autowired
    private FundPreSaleInfoService preSaleInfoService;
    @Autowired
    private FundHistoryValueService fundHistoryValueService;
    @Autowired
    private FundPreSaleRefService fundPreSaleRefService;


    @Override
    public Long insert(FundInfo fundInfo) {
        fundHistoryValueService.queryLatestFundHistoryInfo(fundInfo, false);
        return super.insert(fundInfo);
    }

    @Override
    public List<FundInfo> listGroupedFundsByUserId(Long userId) {
        FundInfo fundInfo = new FundInfo();
        fundInfo.setUserId(userId);
        fundInfo.setIsDeleted(EnumDeleted.NOT_DELETED.val());
        fundInfo.setInStore(EnumYesOrNo.YES.val());

        List<FundInfo> fundInfos = mapper.list(fundInfo);

        if (CollectionUtils.isNotEmpty(fundInfos)) {
            fundInfos = fundInfos.stream()
                    .collect(Collectors.groupingBy(FundInfo::getFundCode))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        // 分组后按各组计算总量
                        String fundCode = entry.getKey();
                        FundInfo resultFund = new FundInfo();
                        resultFund.setFundCode(fundCode);
                        resultFund.setUserId(userId);
                        resultFund.setPurchaseAmount(BigDecimal.ZERO);
                        resultFund.setPurchaseCost(BigDecimal.ZERO);
                        resultFund.setPurchaseFee(BigDecimal.ZERO);
                        resultFund = entry.getValue().stream().reduce(resultFund, (f1, f2) -> {
                            f1.setPurchaseAmount(f1.getPurchaseAmount().add(f2.getPurchaseAmount()));
                            f1.setPurchaseCost(f1.getPurchaseCost().add(f2.getPurchaseCost()));
                            f1.setPurchaseFee(f1.getPurchaseFee().add(f2.getPurchaseFee()));
                            f1.setFundName(f2.getFundName());
                            return f1;
                        });
                        if (resultFund.getPurchaseAmount().compareTo(BigDecimal.ZERO) > 0) {
                            // 平均成本净值
                            resultFund.setPurchaseValue(resultFund.getPurchaseCost().divide(resultFund.getPurchaseAmount(), 4, BigDecimal.ROUND_HALF_UP));
                        } else {
                            logger.warn("fund amount is zero:{}", JSON.toJSONString(resultFund));
                        }
                        return resultFund;
                    })
                    .collect(Collectors.toList());
        }
        return fundInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean preMarkFundsAsSold(List<Long> fundIds, BigDecimal soldFeeRate, String assessmentDate) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(fundIds), "基金id列表不能为空");
        Preconditions.checkState(soldFeeRate != null, "基金售出费率不能为空");
        Preconditions.checkState(StringUtils.isNotEmpty(assessmentDate), "基金估算日期不能为空");
        List<FundInfo> inStoreFunds = mapper.listInStoreFunds(fundIds, UserIdContainer.getUserId());
        if (CollectionUtils.isNotEmpty(inStoreFunds)) {
            inStoreFunds.forEach(fundInfo -> {
                fundInfo.setInStore(EnumYesOrNo.NO.val());
                super.update(fundInfo);
            });

            FundPreSaleInfo preSaleInfo = new FundPreSaleInfo();
            preSaleInfo.setConverted(EnumYesOrNo.NO.val());
            preSaleInfo.setFundCode(inStoreFunds.get(0).getFundCode());
            preSaleInfo.setFundName(inStoreFunds.get(0).getFundName());
            preSaleInfo.setUserId(UserIdContainer.getUserId());

            BigDecimal purchaseCost = BigDecimal.ZERO;
            BigDecimal purchaseFee = BigDecimal.ZERO;
            BigDecimal soldAmount = BigDecimal.ZERO;
            BigDecimal assessmentSoldIncome = BigDecimal.ZERO;
            BigDecimal assessmentSoldFee = BigDecimal.ZERO;


            FundHistoryValue latestFundValue = fundHistoryValueService.getFundLatestValue(preSaleInfo.getFundCode(), assessmentDate);
            boolean hasAssessmentValue = latestFundValue != null;

            for (FundInfo fundInfo : inStoreFunds) {
                purchaseCost = purchaseCost.add(fundInfo.getPurchaseCost());
                purchaseFee = purchaseFee.add(fundInfo.getPurchaseFee());
                soldAmount = soldAmount.add(fundInfo.getPurchaseAmount());
                if (hasAssessmentValue) {
                    assessmentSoldIncome = assessmentSoldIncome.add(latestFundValue.getAssessmentValue().multiply(fundInfo.getPurchaseAmount()));
                }
            }
            if (hasAssessmentValue) {
                assessmentSoldFee = assessmentSoldIncome.multiply(soldFeeRate)
                        .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                assessmentSoldIncome = assessmentSoldIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
                preSaleInfo.setAssessmentValue(latestFundValue.getAssessmentValue());
            }

            preSaleInfo.setAssessmentSoldFee(assessmentSoldFee);
            preSaleInfo.setAssessmentSoldIncome(assessmentSoldIncome);
            preSaleInfo.setSoldAmount(soldAmount);
            preSaleInfo.setPurchaseCost(purchaseCost);
            preSaleInfo.setPurchaseFee(purchaseFee);
            LocalDate localDate = LocalDate.parse(assessmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 将估算日期作为卖出日期
            preSaleInfo.setSoldDate(Date.from(localDate.atStartOfDay(TimeConstants.CHINA_ZONE).toInstant()));
            // 成本净值为买入总支出除以买入（卖出）份额
            preSaleInfo.setCostValue(purchaseCost.divide(soldAmount, 4, BigDecimal.ROUND_HALF_UP));

            Long preSaleId = preSaleInfoService.insert(preSaleInfo);
            if (preSaleId > 0) {
                inStoreFunds.forEach(fundInfo -> {
                    FundPreSaleRef soldRef = new FundPreSaleRef();
                    soldRef.setFundId(fundInfo.getId());
                    soldRef.setFundPreSaleId(preSaleId);
                    fundPreSaleRefService.insert(soldRef);
                });
                return true;
            } else {
                throw new BaseBusinessException("保存预售信息失败");
            }
        }
        return false;
    }

    @Override
    public List<FundExistenceCheck> checkFundsExistence(List<FundExistenceCheck> fundCheckList) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(fundCheckList), "待检测列表不能为空");
        List<FundInfo> userFundsList = mapper.listUserExistsFunds(UserIdContainer.getUserId(),
                fundCheckList.stream().map(FundExistenceCheck::getFundCode)
                        .distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(userFundsList)) {
            return Collections.emptyList();
        }
        Map<String, List<FundInfo>> resultMap = userFundsList.stream().collect(Collectors.groupingBy(FundInfo::getFundCode));
        List<FundExistenceCheck> resultList = new CopyOnWriteArrayList<>();

        fundCheckList.parallelStream().forEach(fundCheck -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<FundInfo> fundInfos = resultMap.get(fundCheck.getFundCode());
            if (CollectionUtils.isNotEmpty(fundInfos)) {
                for (FundInfo fundInfo : fundInfos) {
                    String confirmDate = fundInfo.getConfirmDate().toInstant().atZone(TimeConstants.CHINA_ZONE).format(formatter);
                    if (confirmDate.equals(fundCheck.getConfirmDate()) && fundInfo.getPurchaseAmount().compareTo(fundCheck.getPurchaseAmount()) == 0) {
                        resultList.add(fundCheck);
                    }
                }
            }
        });

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddFunds(List<FundAddModel> fundInfoList) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(fundInfoList), "待增加基金列表不能为空");
        final Long userId = UserIdContainer.getUserId();
        List<FundInfo> forAddFunds = new ArrayList<>();
        fundInfoList.forEach(addFund -> {
            FundInfo fundInfo = new FundInfo();
            fundInfo.setInStore(EnumYesOrNo.YES.val());
            fundInfo.setUserId(userId);
            fundInfo.setFundName(addFund.getFundName());
            fundInfo.setFundCode(addFund.getFundCode());
            fundInfo.setPurchaseAmount(new BigDecimal(addFund.getPurchaseAmount()));
            fundInfo.setPurchaseValue(new BigDecimal(addFund.getPurchaseValue()));
            fundInfo.setPurchaseCost(new BigDecimal(addFund.getPurchaseCost()));
            fundInfo.setPurchaseFee(new BigDecimal(addFund.getPurchaseFee()));
            fundInfo.setConfirmDate(addFund.getPurchaseConfirmDate());
            fundInfo.setPurchaseDate(addFund.getPurchaseDate());
            fundInfo.setModifyTime(new Date());
            fundInfo.setCreateTime(new Date());
            fundInfo.setIsDeleted(EnumYesOrNo.NO.val());
            fundInfo.setVersion(0);
            forAddFunds.add(fundInfo);
        });
        // 更新历史数据
        fundHistoryValueService.updateFundHistoryValues();
        return mapper.batchInsert(forAddFunds) > 0;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, BaseBusinessException.class})
    public boolean preSalePortion(FundPreSalePortionRequest request) {
        FundInfo fundInfo = mapper.getById(request.getId(), UserIdContainer.getUserId());
        if (fundInfo != null) {
            BigDecimal saleAmount = request.getSaleAmount();
            if (saleAmount.compareTo(fundInfo.getPurchaseAmount()) <= 0) {
                BigDecimal restAmount = fundInfo.getPurchaseAmount().subtract(saleAmount);

                Long saleFundId = null;
                if (restAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal oneHundred = new BigDecimal("100");
                    BigDecimal salePercent = saleAmount.multiply(oneHundred).divide(fundInfo.getPurchaseAmount(), BigDecimal.ROUND_HALF_UP);
                    BigDecimal salePurchaseCost = salePercent.multiply(fundInfo.getPurchaseCost()).divide(oneHundred, 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal salePurchaseFee = salePercent.multiply(fundInfo.getPurchaseFee()).divide(oneHundred, 2, BigDecimal.ROUND_HALF_UP);
                    FundInfo saleFund = new FundInfo();
                    BeanUtils.copyProperties(fundInfo, saleFund);
                    saleFund.setPurchaseAmount(saleAmount);
                    saleFund.setPurchaseCost(salePurchaseCost);
                    saleFund.setPurchaseFee(salePurchaseFee);
                    saleFund.setId(null);
                    // 售出的记录下来
                    saleFundId = super.insert(saleFund);
                    // 更新剩余份额
                    fundInfo.setPurchaseAmount(restAmount);
                    fundInfo.setPurchaseCost(fundInfo.getPurchaseCost().subtract(salePurchaseCost));
                    fundInfo.setPurchaseFee(fundInfo.getPurchaseFee().subtract(salePurchaseFee));
                    super.update(fundInfo);
                } else {
                    // 全部卖出
                    saleFundId = fundInfo.getId();
                }

                // 标记为卖出
                if (preMarkFundAsSold(saleFundId, UserIdContainer.getUserId(), request.getSaleFeeRate(), request.getAssessmentDate())) {
                    return true;
                } else {
                    throw new BaseBusinessException("预售出基金信息保存失败");
                }
            }
        }
        return false;
    }


    private boolean preMarkFundAsSold(Long saleFundId, Long userId, BigDecimal soldFeeRate, String assessmentDate) {
        FundInfo saleFund = mapper.getById(saleFundId, userId);
        if (saleFund == null) {
            throw new IllegalStateException("sale fund should never by null");
        }
        FundPreSaleInfo preSaleInfo = new FundPreSaleInfo();
        preSaleInfo.setFundCode(saleFund.getFundCode());
        preSaleInfo.setFundName(saleFund.getFundName());
        preSaleInfo.setConverted(EnumYesOrNo.NO.val());
        preSaleInfo.setUserId(userId);
        preSaleInfo.setPurchaseCost(saleFund.getPurchaseCost());
        preSaleInfo.setPurchaseFee(saleFund.getPurchaseFee());
        preSaleInfo.setSoldAmount(saleFund.getPurchaseAmount());
        // 将估算日期作为卖出日期
        LocalDate localDate = LocalDate.parse(assessmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        preSaleInfo.setSoldDate(Date.from(localDate.atStartOfDay(TimeConstants.CHINA_ZONE).toInstant()));
        // 成本净值为买入总支出除以买入（卖出）份额
        preSaleInfo.setCostValue(saleFund.getPurchaseCost().divide(saleFund.getPurchaseAmount(), 4, BigDecimal.ROUND_HALF_UP));

        FundHistoryValue latestFundValue = fundHistoryValueService.getFundLatestValue(preSaleInfo.getFundCode(), assessmentDate);
        if (latestFundValue != null) {
            BigDecimal assessmentSoldIncome = latestFundValue.getAssessmentValue().multiply(saleFund.getPurchaseAmount());
            BigDecimal assessmentSoldFee = assessmentSoldIncome.multiply(soldFeeRate)
                    .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            preSaleInfo.setAssessmentValue(latestFundValue.getAssessmentValue());
            preSaleInfo.setAssessmentSoldFee(assessmentSoldFee.setScale(2, BigDecimal.ROUND_HALF_UP));
            preSaleInfo.setAssessmentSoldIncome(assessmentSoldIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
            preSaleInfo.setAssessmentValue(latestFundValue.getAssessmentValue());
        }
        saleFund.setInStore(EnumYesOrNo.NO.val());
        return mapper.update(saleFund) > 0 && preSaleInfoService.insert(preSaleInfo) > 0;
    }
}
