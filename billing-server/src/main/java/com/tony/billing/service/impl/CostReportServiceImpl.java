package com.tony.billing.service.impl;

import com.tony.billing.constants.InOutType;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumHidden;
import com.tony.billing.dao.mapper.CostReportMapper;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.RawReportEntity;
import com.tony.billing.entity.ReportEntity;
import com.tony.billing.entity.query.ReportEntityQuery;
import com.tony.billing.service.api.CostReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by TonyJiang on 2017/6/11.
 */
@Service
@Slf4j
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

    @Override
    public List<ReportEntity> getReportInfoBetween(String startDate, String endDate, Long userId) {

        List<CostRecord> costRecords = costReportMapper.getAllCostInfoBetween(startDate, endDate, userId);
        return costRecords.parallelStream()
                .collect(Collectors.groupingByConcurrent(record -> record.getCostCreateTime().substring(0, 10)))
                .entrySet()
                .parallelStream()
                .map(entry -> {
                            String date = entry.getKey();
                            ReportEntity reportEntity = new ReportEntity(date, new RawReportEntity());
                            List<CostRecord> records = entry.getValue();
                            if (CollectionUtils.isNotEmpty(records)) {
                                long totalCost = 0L;
                                long totalCostExceptDeleted = 0L;
                                long totalCostExceptHidden = 0L;
                                long totalCostExceptDeletedAndHidden = 0L;

                                long totalIncomeExceptDeleted = 0L;
                                long totalIncomeExceptHidden = 0L;
                                long totalIncomeExceptDeletedAndHidden = 0L;
                                long totalIncome = 0L;

                                for (CostRecord record : records) {
                                    if (InOutType.COST.equals(record.getInOutType())) {
                                        totalCost += record.getMoney();
                                        int flag = 1;
                                        if (!EnumHidden.HIDDEN.val().equals(record.getIsHidden())) {
                                            totalCostExceptHidden += record.getMoney();
                                            flag = flag << 1;
                                        }
                                        if (!EnumDeleted.DELETED.val().equals(record.getIsDeleted())) {
                                            totalCostExceptDeleted += record.getMoney();
                                            flag = flag << 1;
                                        }
                                        if (flag == 4) {
                                            totalCostExceptDeletedAndHidden += record.getMoney();
                                        }
                                    } else if (InOutType.INCOME.equals(record.getInOutType())) {
                                        totalIncome += record.getMoney();
                                        int flag = 1;
                                        if (!EnumHidden.HIDDEN.val().equals(record.getIsHidden())) {
                                            totalIncomeExceptHidden += record.getMoney();
                                            flag = flag << 1;
                                        }
                                        if (!EnumDeleted.DELETED.val().equals(record.getIsDeleted())) {
                                            totalIncomeExceptDeleted += record.getMoney();
                                            flag = flag << 1;
                                        }
                                        if (flag == 4) {
                                            totalIncomeExceptDeletedAndHidden += record.getMoney();
                                        }
                                    }
                                }
                                RawReportEntity rawReportEntity = new RawReportEntity();
                                rawReportEntity.setTotalCost(totalCost);
                                rawReportEntity.setTotalCostExceptDeletedAndHidden(totalCostExceptDeletedAndHidden);
                                rawReportEntity.setTotalCostExceptDeleted(totalCostExceptDeleted);
                                rawReportEntity.setTotalCostExceptHidden(totalCostExceptHidden);
                                rawReportEntity.setTotalIncome(totalIncome);
                                rawReportEntity.setTotalIncomeExceptDeletedAndHidden(totalIncomeExceptDeletedAndHidden);
                                rawReportEntity.setTotalIncomeExceptDeleted(totalIncomeExceptDeleted);
                                rawReportEntity.setTotalIncomeExceptHidden(totalIncomeExceptHidden);
                                rawReportEntity.calculateAdditional();
                                reportEntity = new ReportEntity(date, rawReportEntity);

                            }
                            return reportEntity;
                        }
                ).collect(Collectors.toList());
    }

}
