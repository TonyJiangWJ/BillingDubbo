package com.tony.billing.request.asset;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2018-11-25
 */
public class AssetDeleteRequest extends BaseRequest {
    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.ASSET)
    private Long assetId;

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
}
