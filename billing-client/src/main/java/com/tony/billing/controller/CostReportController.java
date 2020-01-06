package com.tony.billing.controller;

import com.tony.billing.dto.ReportDTO;
import com.tony.billing.entity.ReportEntity;
import com.tony.billing.request.costreport.CostReportRequest;
import com.tony.billing.request.costreport.DailyCostReportRequest;
import com.tony.billing.response.costrecord.ReportResponse;
import com.tony.billing.service.api.CostReportService;
import com.tony.billing.util.BeanCopyUtil;
import com.tony.billing.util.ResponseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by TonyJiang on 2017/6/10.
 */
@RestController
@RequestMapping("/bootDemo")
public class CostReportController extends BaseController {

    @Reference
    private CostReportService costReportService;

    @RequestMapping(value = "/report/get")
    public ReportResponse getReport(@ModelAttribute("request") CostReportRequest request) {
        ReportResponse response = new ReportResponse();
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDate localDate = LocalDate.now();

            List<String> monthList = new ArrayList<>();
            if (StringUtils.isEmpty(request.getStartMonth()) || StringUtils.isEmpty(request.getEndMonth())) {
                int ngm = -6;
                localDate = localDate.plusMonths(ngm);
                for (int i = 0; i < -ngm; i++) {
                    localDate = localDate.plusMonths(1);
                    monthList.add(localDate.format(dateTimeFormatter));
                }
            } else {
                YearMonth start = YearMonth.parse(request.getStartMonth(), dateTimeFormatter);
                YearMonth end = YearMonth.parse(request.getEndMonth(), dateTimeFormatter);

                do {
                    monthList.add(start.format(dateTimeFormatter));
                    start = start.plusMonths(1);
                } while (start.isBefore(end));
            }
            return getReportResponse(monthList, request.getUserId(), response);
        } catch (Exception e) {
            logger.error("/report/get error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping("/daily/report/get")
    public ReportResponse getDailyCostReport(@ModelAttribute("request") @Validated DailyCostReportRequest reportRequest) {
        ReportResponse response = new ReportResponse();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate startDate = LocalDate.parse(reportRequest.getStartDate(), dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(reportRequest.getEndDate(), dateTimeFormatter);

            List<String> datePrefixes = new ArrayList<>();
            datePrefixes.add(startDate.format(dateTimeFormatter));
            while (startDate.isBefore(endDate)) {
                startDate = startDate.plusDays(1);
                datePrefixes.add(startDate.format(dateTimeFormatter));
            }

            return getReportResponse(datePrefixes, reportRequest.getUserId(), response);
        } catch (Exception e) {
            logger.error("/daily/report/get error", e);
            return ResponseUtil.sysError(response);
        }
    }

    private ReportResponse getReportResponse(List<String> prefixes, Long userId, ReportResponse response) {
        List<ReportEntity> result = costReportService.getReportByDatePrefix(prefixes, userId);
        if (CollectionUtils.isEmpty(result)) {
            ResponseUtil.dataNotExisting(response);
        } else {
            response.setReportList(BeanCopyUtil.copy(result, ReportDTO.class));
            ResponseUtil.success(response);
        }
        return response;
    }
}
