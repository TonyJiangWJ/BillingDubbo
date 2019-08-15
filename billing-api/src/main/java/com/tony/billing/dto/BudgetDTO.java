package com.tony.billing.dto;

import com.tony.billing.dto.base.BaseDTO;

import java.util.List;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetDTO extends BaseDTO {

    private Long id;
    private String budgetName;
    private String budgetMoney;
    private String yearMonth;

    private List<TagInfoDTO> tagInfos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(String budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<TagInfoDTO> getTagInfos() {
        return tagInfos;
    }

    public void setTagInfos(List<TagInfoDTO> tagInfos) {
        this.tagInfos = tagInfos;
    }
}
