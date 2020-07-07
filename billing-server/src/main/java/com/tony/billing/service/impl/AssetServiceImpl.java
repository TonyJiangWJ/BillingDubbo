package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.dao.mapper.AssetMapper;
import com.tony.billing.dto.AssetDTO;
import com.tony.billing.entity.Asset;
import com.tony.billing.entity.AssetTypes;
import com.tony.billing.model.AssetModel;
import com.tony.billing.service.api.AssetService;
import com.tony.billing.service.api.AssetTypesService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl extends AbstractServiceImpl<Asset, AssetMapper> implements AssetService {

    @Resource
    private AssetTypesService assetTypesService;

    @Override
    public List<Asset> listAssetByUserId(Long userId) {
        Asset query = new Asset();
        query.setUserId(userId);
        return super.list(query);
    }

    @Override
    public List<AssetModel> getAssetModelsByUserId(Long userId) {
        assert userId != null;
        Asset query = new Asset();
        query.setUserId(userId);
        List<Asset> assetList = super.list(query);
        List<AssetModel> assetModels = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(assetList)) {
            assetModels = assetList.stream().collect(Collectors.groupingBy(asset -> {
                AssetTypes assetTypes = assetTypesService.getAssetTypeByIdWithCache(asset.getType());
                if (assetTypes != null && StringUtils.isNotEmpty(assetTypes.getParentCode())) {
                    return assetTypes.getParentCode();
                } else {
                    logger.error("未能获取资产主类别 assetId:{} assetTypes:{}", asset.getId(), JSON.toJSONString(assetTypes));
                    return "UNDEFINED";
                }
            })).entrySet()
                    .stream()
                    .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                    .map(stringListEntry -> {
                        AssetModel assetModel = new AssetModel();
                        AssetTypes assetTypes = assetTypesService.getAssetTypeByCodeWithCache(stringListEntry.getKey());
                        if (assetTypes!=null) {
                            assetModel.setType(assetTypes.getTypeDesc());
                        } else {
                            assetModel.setType("未知类型");
                        }
                        AssetSummary assetSummary = stringListEntry.getValue().stream()
                                .reduce(new AssetSummary(),
                                        AssetSummary::addAmount,
                                        (a, b) -> null);
                        assetModel.setAssetList(assetSummary.assetDTOS);
                        assetModel.setTotal(assetSummary.totalAmount);
                        assetModel.setTotalAvailable(assetSummary.availableAmount);
                        return assetModel;
                    }).collect(Collectors.toList());
        }
        return assetModels;
    }


    private class AssetSummary {
        private List<AssetDTO> assetDTOS;
        private Long totalAmount;
        private Long availableAmount;

        AssetSummary() {
            totalAmount = availableAmount = 0L;
            assetDTOS = new ArrayList<>();
        }

        public AssetSummary addAmount(Asset asset) {
            assetDTOS.add(new AssetDTO(asset));
            if (EnumYesOrNo.YES.getCode().equals(asset.getAvailable())) {
                availableAmount += asset.getAmount();
            }
            totalAmount += asset.getAmount();
            return this;
        }

    }

    @Override
    public Asset getAssetInfoById(Long id) {
        return super.getById(id);
    }

    @Override
    public boolean modifyAssetInfoById(Asset asset) {
        return super.update(asset);
    }

    @Override
    public Long addAsset(Asset asset) {
        asset.setUserId(UserIdContainer.getUserId());
        AssetTypes assetTypes = assetTypesService.getById(asset.getType());
        boolean isAssetTypeValid = assetTypes != null && (assetTypes.getUserId().equals(-1L) || assetTypes.getUserId().equals(asset.getUserId()));
        if (isAssetTypeValid) {
            if (StringUtils.isNotEmpty(asset.getExtName())) {
                asset.setName(asset.getExtName());
            } else {
                asset.setName(assetTypes.getTypeDesc());
            }
            return super.insert(asset);
        } else {
            return -1L;
        }
    }

    @Override
    public boolean deleteAsset(Long assetId) {
        return super.deleteById(assetId);
    }

}
