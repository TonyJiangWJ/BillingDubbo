package com.tony.billing.response.asset;

import com.tony.billing.dto.AssetManageDTO;
import com.tony.billing.model.AssetTypeModel;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author TonyJiang 2018/6/21
 */
public class AssetManageResponse extends BaseResponse {
    private AssetManageDTO assetManage;

    public AssetManageDTO getAssetManage() {
        return assetManage;
    }

    public void setAssetManage(AssetManageDTO assetManage) {
        this.assetManage = assetManage;
    }
}
