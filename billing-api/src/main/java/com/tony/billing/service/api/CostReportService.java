package com.tony.billing.service.api;

import com.tony.billing.entity.ReportEntity;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/11.
 */
public interface CostReportService {
    /**
     * 每月收支报告
     * @param datePrefix 月份列表或日期列表
     * @param userId 用户id
     * @return 每月的收支统计信息
     */
    @Reference(timeout = 10000)
    List<ReportEntity> getReportByDatePrefix(List<String> datePrefix, Long userId);

    @Reference(timeout = 10000)
    List<ReportEntity> getReportInfoBetween(String startDate, String endDate, Long userId);
}
