package com.tony.billing.request.costrecord;

import com.tony.billing.entity.CostRecord;
import com.tony.billing.request.BaseRequest;

import java.lang.reflect.Field;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public class CostRecordPageRequest extends BaseRequest {
    private Integer pageNo;
    private Integer pageSize;
    private Integer isDeleted;
    private Integer isHidden;
    private String inOutType;
    private String startDate;
    private String endDate;
    private String sort;
    private String orderBy;
    private String content;
    private Boolean showTags;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = "";
        Field[] fields = CostRecord.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(orderBy)) {
                this.orderBy = field.getName();
                break;
            }
        }
    }

    public Integer getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }

    public String getInOutType() {
        return inOutType;
    }

    public void setInOutType(String inOutType) {
        this.inOutType = inOutType;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getShowTags() {
        return showTags;
    }

    public void setShowTags(Boolean showTags) {
        this.showTags = showTags;
    }
}
