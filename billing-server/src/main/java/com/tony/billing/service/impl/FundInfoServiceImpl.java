package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundHistoryValue;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.entity.FundPreSoldInfo;
import com.tony.billing.entity.FundPreSoldRef;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.model.FundAddModel;
import com.tony.billing.model.FundExistenceCheck;
import com.tony.billing.service.api.FundHistoryValueService;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.api.FundPreSoldInfoService;
import com.tony.billing.service.api.FundPreSoldRefService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
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
    private FundPreSoldInfoService preSoldInfoService;
    @Autowired
    private FundHistoryValueService fundHistoryValueService;
    @Autowired
    private FundPreSoldRefService fundPreSoldRefService;


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
                            resultFund.setPurchaseValue(resultFund.getPurchaseCost().divide(resultFund.getPurchaseAmount(), BigDecimal.ROUND_HALF_UP));
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

            FundPreSoldInfo preSoldInfo = new FundPreSoldInfo();
            preSoldInfo.setConverted(EnumYesOrNo.NO.val());
            preSoldInfo.setFundCode(inStoreFunds.get(0).getFundCode());
            preSoldInfo.setFundName(inStoreFunds.get(0).getFundName());
            preSoldInfo.setUserId(UserIdContainer.getUserId());

            BigDecimal purchaseCost = BigDecimal.ZERO;
            BigDecimal purchaseFee = BigDecimal.ZERO;
            BigDecimal soldAmount = BigDecimal.ZERO;
            BigDecimal assessmentSoldIncome = BigDecimal.ZERO;
            BigDecimal assessmentSoldFee = BigDecimal.ZERO;


            FundHistoryValue latestFundValue = fundHistoryValueService.getFundLatestValue(preSoldInfo.getFundCode(), assessmentDate);
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
                preSoldInfo.setAssessmentValue(latestFundValue.getAssessmentValue());
            }

            preSoldInfo.setAssessmentSoldFee(assessmentSoldFee);
            preSoldInfo.setAssessmentSoldIncome(assessmentSoldIncome);
            preSoldInfo.setSoldAmount(soldAmount);
            preSoldInfo.setPurchaseCost(purchaseCost);
            preSoldInfo.setPurchaseFee(purchaseFee);
            LocalDate localDate = LocalDate.parse(assessmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            preSoldInfo.setSoldDate(Date.from(localDate.atStartOfDay(TimeConstants.CHINA_ZONE).toInstant()));
            preSoldInfo.setCostValue(purchaseCost.divide(soldAmount, 4, BigDecimal.ROUND_HALF_UP));

            Long preSoldId = preSoldInfoService.insert(preSoldInfo);
            if (preSoldId > 0) {
                inStoreFunds.forEach(fundInfo -> {
                    FundPreSoldRef soldRef = new FundPreSoldRef();
                    soldRef.setFundId(fundInfo.getId());
                    soldRef.setFundPreSoldId(preSoldId);
                    fundPreSoldRefService.insert(soldRef);
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
}
