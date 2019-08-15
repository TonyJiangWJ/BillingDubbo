package com.tony.billing.response.costrecord;

import com.tony.billing.dto.ReportDTO;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/10.
 */
public class ReportResponse extends BaseResponse {
    private List<ReportDTO> reportList;

    public List<ReportDTO> getReportList() {
        return reportList;
    }

    public void setReportList(List<ReportDTO> reportList) {
        this.reportList = reportList;
    }
}
