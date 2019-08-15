package com.tony.billing.response.budget;

import com.tony.billing.model.BudgetReportModel;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-24
 */
public class BudgetOverviewResponse extends BaseResponse {
    private List<BudgetReportModel> reportModelList;

    public List<BudgetReportModel> getReportModelList() {
        return reportModelList;
    }

    public void setReportModelList(List<BudgetReportModel> reportModelList) {
        this.reportModelList = reportModelList;
    }
}
