package com.tony.billing.request.asset;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/22
 */
public class AssetDetailRequest extends BaseRequest {
    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.ASSET)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
