package com.tony.billing.entity;

import com.tony.billing.util.MoneyUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author by TonyJiang on 2017/6/11.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
public class ReportEntity implements Serializable {
    private String month;
    private String totalCost;
    private String totalCostExceptDeleted;
    private String totalCostExceptHidden;
    private String totalCostExceptDeletedAndHidden;
    private String totalCostHidden;
    private String totalCostDeleted;
    private String totalCostDeletedAndHidden;
    private String totalIncome;
    private String totalIncomeExceptDeleted;
    private String totalIncomeExceptHidden;
    private String totalIncomeExceptDeletedAndHidden;
    private String totalIncomeHidden;
    private String totalIncomeDeleted;
    private String totalIncomeDeletedAndHidden;

    public ReportEntity() {}

    public ReportEntity(String datePrefix, RawReportEntity rawReportEntity) {
        month = datePrefix;
        totalCost = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCost());
        totalCostExceptDeleted = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostExceptDeleted());
        totalCostExceptHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostExceptHidden());
        totalCostExceptDeletedAndHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostExceptDeletedAndHidden());
        totalCostHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostHidden());
        totalCostDeleted = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostDeleted());
        totalCostDeletedAndHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalCostDeletedAndHidden());
        totalIncome = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncome());
        totalIncomeExceptDeleted = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeExceptDeleted());
        totalIncomeExceptHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeExceptHidden());
        totalIncomeExceptDeletedAndHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeExceptDeletedAndHidden());
        totalIncomeHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeHidden());
        totalIncomeDeleted = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeDeleted());
        totalIncomeDeletedAndHidden = MoneyUtil.fen2Yuan(rawReportEntity.getTotalIncomeDeletedAndHidden());
    }

}
