package com.tony.billing.request.budget;

import com.tony.billing.request.BaseRequest;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetCostQueryRequest extends BaseRequest {
    private String year;
    private String tagName;
    private Integer month;
    private Integer isDeleted;
    private Integer isHidden;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }
}
