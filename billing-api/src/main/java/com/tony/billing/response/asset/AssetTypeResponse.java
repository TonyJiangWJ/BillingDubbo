package com.tony.billing.response.asset;

import com.tony.billing.model.AssetTypeModel;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author jiangwj20966 2018/4/24
 */
public class AssetTypeResponse extends BaseResponse {
    private List<AssetTypeModel> assetTypes;

    public List<AssetTypeModel> getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(List<AssetTypeModel> assetTypes) {
        this.assetTypes = assetTypes;
    }
}
