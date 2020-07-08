package com.tony.billing.service.api;

import com.tony.billing.entity.AssetTypes;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @author TonyJiang 2018/6/21
 */
public interface AssetTypesService {

    List<AssetTypes> selectAssetTypeList();

    List<AssetTypes> selectLiabilityTypeList();

    List<AssetTypes> selectAssetTypeListByParent(String parentCode);

    List<AssetTypes> selectLiabilityTypeListByParent(String parentCode);

    List<AssetTypes> getAssetTypeByCondition(AssetTypes condition);

    @Reference(retries = 0)
    Long insert(AssetTypes assetTypes);

    boolean update(AssetTypes assetTypes);

    AssetTypes getById(Long id);

    AssetTypes getAssetTypeByIdWithCache(Long id);

    AssetTypes getAssetTypeByCodeWithCache(String typeCode);
}
