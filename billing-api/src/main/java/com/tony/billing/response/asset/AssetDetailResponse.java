package com.tony.billing.response.asset;

import com.tony.billing.dto.AssetDTO;
import com.tony.billing.response.BaseResponse;

/**
 * <p>
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/22
 */
public class AssetDetailResponse extends BaseResponse {
    private AssetDTO assetInfo;

    public AssetDTO getAssetInfo() {
        return assetInfo;
    }

    public void setAssetInfo(AssetDTO assetInfo) {
        this.assetInfo = assetInfo;
    }
}
