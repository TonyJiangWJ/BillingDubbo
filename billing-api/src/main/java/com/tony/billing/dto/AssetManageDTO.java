package com.tony.billing.dto;

import com.tony.billing.dto.base.BaseDTO;
import com.tony.billing.model.AssetModel;
import com.tony.billing.model.LiabilityModel;
import com.tony.billing.model.MonthLiabilityModel;

import java.util.List;

/**
 * @author TonyJiang on 2018/2/12
 */
public class AssetManageDTO extends BaseDTO {

    private Long totalAsset;
    private Long totalLiability;
    /**
     * 净资产
     */
    private Long cleanAsset;
    /**
     * 可使用的金额
     */
    private Long availableAsset;
    private List<AssetModel> assetModels;
    private List<LiabilityModel> liabilityModels;
    private List<MonthLiabilityModel> monthLiabilityModels;

    public Long getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(Long totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Long getTotalLiability() {
        return totalLiability;
    }

    public void setTotalLiability(Long totalLiability) {
        this.totalLiability = totalLiability;
    }

    public Long getCleanAsset() {
        return cleanAsset;
    }

    public void setCleanAsset(Long cleanAsset) {
        this.cleanAsset = cleanAsset;
    }

    public Long getAvailableAsset() {
        return availableAsset;
    }

    public void setAvailableAsset(Long availableAsset) {
        this.availableAsset = availableAsset;
    }

    public List<AssetModel> getAssetModels() {
        return assetModels;
    }

    public void setAssetModels(List<AssetModel> assetModels) {
        this.assetModels = assetModels;
    }

    public List<LiabilityModel> getLiabilityModels() {
        return liabilityModels;
    }

    public void setLiabilityModels(List<LiabilityModel> liabilityModels) {
        this.liabilityModels = liabilityModels;
    }

    public List<MonthLiabilityModel> getMonthLiabilityModels() {
        return monthLiabilityModels;
    }

    public void setMonthLiabilityModels(List<MonthLiabilityModel> monthLiabilityModels) {
        this.monthLiabilityModels = monthLiabilityModels;
    }
}
