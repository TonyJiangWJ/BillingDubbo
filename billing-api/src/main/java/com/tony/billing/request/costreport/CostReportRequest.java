package com.tony.billing.request.costreport;

import com.tony.billing.request.BaseRequest;

/**
 * @author jiangwj20966 on 2017/11/15.
 */
public class CostReportRequest extends BaseRequest {
    private String startMonth;
    private String endMonth;

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }
}
