package com.tony.billing.model;

import com.tony.billing.dto.BudgetReportItemDTO;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangwenjie 2019-03-22
 */
public class BudgetReportModel {
    private String yearMonthInfo;
    private Long totalAmount;
    private Long budgetUsed;
    private Long remain;
    private Long noBudgetUsed;
    private List<BudgetReportItemDTO> reportItems;

    public BudgetReportModel() {
        this.totalAmount = budgetUsed = remain = noBudgetUsed = 0L;
        reportItems = new ArrayList<>();
    }

    public String getYearMonthInfo() {
        return yearMonthInfo;
    }

    public void setYearMonthInfo(String yearMonthInfo) {
        this.yearMonthInfo = yearMonthInfo;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getBudgetUsed() {
        return budgetUsed;
    }

    public void setBudgetUsed(Long budgetUsed) {
        this.budgetUsed = budgetUsed;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public Long getNoBudgetUsed() {
        return noBudgetUsed;
    }

    public void setNoBudgetUsed(Long noBudgetUsed) {
        this.noBudgetUsed = noBudgetUsed;
    }

    public List<BudgetReportItemDTO> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<BudgetReportItemDTO> reportItems) {
        this.reportItems = reportItems;
    }

    public void addBudgetInfos(BudgetReportItemDTO item) {
        this.reportItems.add(item);
    }

    public BudgetReportModel build() {
        if (CollectionUtils.isNotEmpty(this.reportItems)) {
            reportItems.forEach(budget -> this.totalAmount += budget.getAmount());
        }
        this.remain = this.totalAmount - this.budgetUsed - this.noBudgetUsed;
        return this;
    }
}
