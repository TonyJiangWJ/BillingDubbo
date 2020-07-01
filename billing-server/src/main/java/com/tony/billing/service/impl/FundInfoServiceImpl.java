package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.base.AbstractService;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangwenjie 2020/6/28
 */
@Service
public class FundInfoServiceImpl extends AbstractService<FundInfo, FundInfoMapper> implements FundInfoService {

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
    public boolean markFundsAsSold(List<Long> fundIds) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(fundIds), "基金id列表不能为空");
        List<FundInfo> inStoreFunds = mapper.listInStoreFunds(fundIds, UserIdContainer.getUserId());
        if (CollectionUtils.isNotEmpty(inStoreFunds)) {
            inStoreFunds.forEach(fundInfo -> {
                fundInfo.setInStore(EnumYesOrNo.NO.val());
                super.update(fundInfo);
            });
        }
        return false;
    }
}
