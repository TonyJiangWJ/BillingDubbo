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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            List<String> monthList = new ArrayList<>();
            if (StringUtils.isEmpty(request.getStartMonth()) || StringUtils.isEmpty(request.getEndMonth())) {
                int ngm = -6;
                calendar.add(Calendar.MONTH, ngm);
                for (int i = 0; i < -ngm; i++) {
                    calendar.add(Calendar.MONTH, 1);
                    monthList.add(sdf.format(calendar.getTime()));
                }
            } else {
                Date startDate = sdf.parse(request.getStartMonth());
                Date endDate = sdf.parse(request.getEndMonth());
                calendar.setTime(startDate);
                Date tempDate;
                do {
                    monthList.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.MONTH, 1);
                    tempDate = calendar.getTime();
                } while (tempDate.compareTo(endDate) <= 0);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = simpleDateFormat.parse(reportRequest.getStartDate());
            Date endDate = simpleDateFormat.parse(reportRequest.getEndDate());
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);

            long dayBetween = (endDate.getTime() - startDate.getTime()) / (3600 * 24 * 1000);
            List<String> datePrefixes = new ArrayList<>();
            while (dayBetween-- >= 0) {
                datePrefixes.add(simpleDateFormat.format(start.getTime()));
                start.add(Calendar.DATE, 1);
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
