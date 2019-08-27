package com.tony.billing.service.impl;

import com.tony.billing.constants.enums.EnumTypeIdentify;
import com.tony.billing.dao.mapper.AssetTypesMapper;
import com.tony.billing.entity.AssetTypes;
import com.tony.billing.service.api.AssetTypesService;
import com.tony.billing.service.base.AbstractService;
import com.tony.billing.util.RedisUtils;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author TonyJiang 2018/6/21
 */
@Service
public class AssetTypesServiceImpl extends AbstractService<AssetTypes, AssetTypesMapper> implements AssetTypesService {


    @Resource
    private RedisUtils redisUtils;

    @Override
    public List<AssetTypes> selectAssetTypeList() {
        return listTargetParentTypes(EnumTypeIdentify.ASSET);
    }

    @Override
    public List<AssetTypes> selectLiabilityTypeList() {
        return listTargetParentTypes(EnumTypeIdentify.LIABILITY);
    }

    private List<AssetTypes> listTargetParentTypes(EnumTypeIdentify enumTypeIdentify) {
        List<AssetTypes> assetTypesList = super.list(
                Stream.generate(() -> {
                    AssetTypes condition = new AssetTypes();
                    condition.setTypeIdentify(enumTypeIdentify.getIdentify());
                    condition.setUserId(UserIdContainer.getUserId());
                    return condition;
                }).findAny().get()
        );
        if (CollectionUtils.isNotEmpty(assetTypesList)) {
            return assetTypesList.stream().filter(assetTypes -> StringUtils.isEmpty(assetTypes.getParentCode())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<AssetTypes> selectAssetTypeListByParent(String parentCode) {
        return super.list(
                Stream.generate(() -> {
                    AssetTypes condition = new AssetTypes();
                    condition.setParentCode(parentCode);
                    condition.setTypeIdentify(EnumTypeIdentify.ASSET.getIdentify());
                    condition.setUserId(UserIdContainer.getUserId());
                    return condition;
                }).findAny().get()
        );
    }

    @Override
    public List<AssetTypes> selectLiabilityTypeListByParent(String parentCode) {
        return super.list(
                Stream.generate(() -> {
                    AssetTypes condition = new AssetTypes();
                    condition.setParentCode(parentCode);
                    condition.setTypeIdentify(EnumTypeIdentify.LIABILITY.getIdentify());
                    condition.setUserId(UserIdContainer.getUserId());
                    return condition;
                }).findAny().get()
        );
    }

    @Override
    public List<AssetTypes> getAssetTypeByCondition(AssetTypes condition) {
        return super.list(condition);
    }

    @Override
    public AssetTypes getAssetTypeByIdWithCache(Long id) {
        String redisCacheKey = "TRANSIENT_ASSET_TYPE_BY_ID_" + UserIdContainer.getUserId() + "_" + id;
        AssetTypes assetTypes = redisUtils.get(redisCacheKey, AssetTypes.class).orElse(null);
        if (assetTypes == null) {
            assetTypes = mapper.getById(id, UserIdContainer.getUserId());
            if (assetTypes != null) {
                redisUtils.set(redisCacheKey, assetTypes, redisUtils.getTransientTime());
            }
        }
        return assetTypes;
    }

    @Override
    public AssetTypes getAssetTypeByCodeWithCache(String typeCode) {
        String redisCacheKey = "TRANSIENT_ASSET_TYPE_BY_ID_" + UserIdContainer.getUserId() + "_" + typeCode;
        AssetTypes assetTypes = redisUtils.get(redisCacheKey, AssetTypes.class).orElse(null);
        if (assetTypes == null) {
            assetTypes = mapper.getByTypeCode(typeCode, UserIdContainer.getUserId());
            if (assetTypes != null) {
                redisUtils.set(redisCacheKey, assetTypes, redisUtils.getTransientTime());
            }
        }
        return assetTypes;
    }

}
