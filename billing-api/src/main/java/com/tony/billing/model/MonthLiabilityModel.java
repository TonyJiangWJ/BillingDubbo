package com.tony.billing.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 每月分期还款总额
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/16
 */
public class MonthLiabilityModel {

    private String month;
    private Long total;
    private Long assetAfterThisMonth;
    private List<LiabilityModel> liabilityModels;
    private Long remain;

    public MonthLiabilityModel() {
        liabilityModels = new ArrayList<>();
        total = 0L;
    }

    public MonthLiabilityModel(String month) {
        this.month = month;
        total = 0L;
        liabilityModels = new ArrayList<>();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<LiabilityModel> getLiabilityModels() {
        return liabilityModels;
    }

    public void setLiabilityModels(List<LiabilityModel> liabilityModels) {
        this.liabilityModels = liabilityModels;
    }

    public Long getAssetAfterThisMonth() {
        return assetAfterThisMonth;
    }

    public void setAssetAfterThisMonth(Long assetAfterThisMonth) {
        this.assetAfterThisMonth = assetAfterThisMonth;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }
}
