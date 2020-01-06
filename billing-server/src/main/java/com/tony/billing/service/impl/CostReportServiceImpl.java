package com.tony.billing.service.impl;

import com.tony.billing.constants.InOutType;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumHidden;
import com.tony.billing.dao.mapper.CostReportMapper;
import com.tony.billing.entity.RawReportEntity;
import com.tony.billing.entity.ReportEntity;
import com.tony.billing.entity.query.ConditionalReportEntityQuery;
import com.tony.billing.entity.query.ReportEntityQuery;
import com.tony.billing.service.api.CostReportService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by TonyJiang on 2017/6/11.
 */
@Service
public class CostReportServiceImpl implements CostReportService {

    @Resource
    private CostReportMapper costReportMapper;

    @Override
    public List<ReportEntity> getReportByDatePrefix(List<String> datePrefixes, Long userId) {
        return datePrefixes.parallelStream().map(
                (dataPrefix) -> getReportInfo(dataPrefix, userId)
        ).collect(Collectors.toList());

    }

    private ReportEntity getReportInfo(String datePrefix, Long userId) {

        ReportEntityQuery query = new ReportEntityQuery();
        query.setDatePrefix(datePrefix);
        query.setUserId(userId);
        RawReportEntity reportEntity = costReportMapper.getReportTypeAmountByCondition(query);
        if (reportEntity != null) {
            return new ReportEntity(datePrefix, reportEntity);
        } else {
            return null;
        }
    }

    private ReportEntity getReportInfoRaw(String datePrefix, Long userId) {
        ConditionalReportEntityQuery conditionalQuery = new ConditionalReportEntityQuery();
        conditionalQuery.setDatePrefix(datePrefix);
        conditionalQuery.setUserId(userId);

        RawReportEntity rawReportEntity = new RawReportEntity();
        conditionalQuery.setInOutType(InOutType.COST);
        rawReportEntity.setTotalCost(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.DELETED.val());
        rawReportEntity.setTotalCostDeleted(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(null);
        conditionalQuery.setIsHidden(EnumHidden.HIDDEN.val());
        rawReportEntity.setTotalCostHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.DELETED.val());
        rawReportEntity.setTotalCostDeletedAndHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.NOT_DELETED.val());
        conditionalQuery.setIsHidden(EnumHidden.NOT_HIDDEN.val());
        rawReportEntity.setTotalCostExceptDeletedAndHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));

        conditionalQuery.setInOutType(InOutType.INCOME);
        conditionalQuery.setIsHidden(null);
        conditionalQuery.setIsDeleted(null);
        rawReportEntity.setTotalIncome(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.DELETED.val());
        rawReportEntity.setTotalIncomeDeleted(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(null);
        conditionalQuery.setIsHidden(EnumHidden.HIDDEN.val());
        rawReportEntity.setTotalIncomeHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.DELETED.val());
        rawReportEntity.setTotalIncomeDeletedAndHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));
        conditionalQuery.setIsDeleted(EnumDeleted.NOT_DELETED.val());
        conditionalQuery.setIsHidden(EnumHidden.NOT_HIDDEN.val());
        rawReportEntity.setTotalIncomeExceptDeletedAndHidden(costReportMapper.getReportAmountByCondition(conditionalQuery));

        rawReportEntity.calculateAdditional();
        return new ReportEntity(datePrefix, rawReportEntity);
    }

}
