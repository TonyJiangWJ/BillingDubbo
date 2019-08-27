package com.tony.billing.service.impl;

import com.tony.billing.dao.mapper.CostReportMapper;
import com.tony.billing.entity.RawReportEntity;
import com.tony.billing.entity.ReportEntity;
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
        return datePrefixes.stream().map(
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

}
