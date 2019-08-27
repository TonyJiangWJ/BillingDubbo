package com.tony.billing.service.api;

import com.tony.billing.entity.Asset;
import com.tony.billing.model.AssetModel;

import java.util.List;

public interface AssetService {

    List<Asset> listAssetByUserId(Long userId);

    List<AssetModel> getAssetModelsByUserId(Long userId);

    /**
     * 根据id获取资产信息
     *
     * @param id
     * @return
     */
    Asset getAssetInfoById(Long id);

    /**
     * 修改资产信息
     *
     * @param asset
     * @return
     */
    boolean modifyAssetInfoById(Asset asset);

    /**
     * 创建资产信息
     *
     * @param asset
     * @return
     */
    Long addAsset(Asset asset);

    /**
     * 删除资产信息
     *
     * @param assetId
     * @return
     */
    boolean deleteAsset(Long assetId);

}
