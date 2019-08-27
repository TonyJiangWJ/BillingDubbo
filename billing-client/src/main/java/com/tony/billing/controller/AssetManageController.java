package com.tony.billing.controller;

import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.dto.AssetDTO;
import com.tony.billing.dto.AssetManageDTO;
import com.tony.billing.entity.Asset;
import com.tony.billing.model.AssetModel;
import com.tony.billing.model.LiabilityModel;
import com.tony.billing.model.MonthLiabilityModel;
import com.tony.billing.request.BaseRequest;
import com.tony.billing.request.asset.AssetAddRequest;
import com.tony.billing.request.asset.AssetDeleteRequest;
import com.tony.billing.request.asset.AssetDetailRequest;
import com.tony.billing.request.asset.AssetUpdateRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.response.asset.AssetDetailResponse;
import com.tony.billing.response.asset.AssetManageResponse;
import com.tony.billing.service.api.AssetService;
import com.tony.billing.service.api.LiabilityService;
import com.tony.billing.util.ResponseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TonyJiang on 2018/2/12
 */
@RestController
@RequestMapping("/bootDemo")
public class AssetManageController extends BaseController {

    @Reference
    private AssetService assetService;
    @Reference
    private LiabilityService liabilityService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/asset/manage")
    public AssetManageResponse assetManage(@ModelAttribute("request") BaseRequest request) {
        AssetManageDTO assetManageDTO = new AssetManageDTO();
        // 计算总资产
        assetManageDTO.setAssetModels(assetService.getAssetModelsByUserId(request.getUserId()));
        assetManageDTO.setTotalAsset(assetManageDTO.getAssetModels()
                .stream().map((AssetModel::getTotal)).reduce(0L, Long::sum));
        // 计算总负债
        assetManageDTO.setLiabilityModels(liabilityService.getLiabilityModelsByUserId(request.getUserId()));
        assetManageDTO.setTotalLiability(assetManageDTO.getLiabilityModels()
                .stream().mapToLong(LiabilityModel::getTotal).reduce(Long::sum).orElse(0L));
        // 计算净资产
        assetManageDTO.setCleanAsset(assetManageDTO.getTotalAsset() - assetManageDTO.getTotalLiability());
        // 计算可以直接使用的金额
        assetManageDTO.setAvailableAsset(assetManageDTO.getAssetModels()
                .stream().mapToLong(AssetModel::getTotalAvailable).sum());
        // 计算每月还款信息
        assetManageDTO.setMonthLiabilityModels(liabilityService.getMonthLiabilityModelsByUserId(request.getUserId()));
        // 计算每月还款后剩余
        calAssetAfterMonth(assetManageDTO);
        AssetManageResponse response = ResponseUtil.success(new AssetManageResponse());
        response.setAssetManage(assetManageDTO);
        return response;
    }

    @RequestMapping("/asset/detail/get")
    public AssetDetailResponse getAssetDetail(@ModelAttribute("request") @Validated AssetDetailRequest request, Model model) {
        AssetDetailResponse response = ResponseUtil.success(new AssetDetailResponse());
        Asset asset = assetService.getAssetInfoById(request.getId());
        if (asset != null && asset.getUserId().equals(request.getUserId())) {
            response.setAssetInfo(new AssetDTO(asset));
        } else {
            response = ResponseUtil.dataNotExisting(new AssetDetailResponse());
        }
        return response;
    }

    @RequestMapping("/asset/update")
    public BaseResponse updateAsset(@ModelAttribute("request") @Validated AssetUpdateRequest request) {
        Asset update = new Asset();
        if (request.getAmount() == null || request.getId() == null || !commonValidate(request)) {
            return ResponseUtil.paramError();
        }
        update.setId(request.getId());
        update.setAmount(request.getAmount());
        update.setExtName(request.getName());
        update.setUserId(request.getUserId());
        update.setVersion(request.getVersion());
        if (StringUtils.isNotEmpty(request.getAvailable())) {
            update.setAvailable("true".equalsIgnoreCase(request.getAvailable()) ? EnumYesOrNo.YES.getCode() : EnumYesOrNo.NO.getCode());
        }
        if (assetService.modifyAssetInfoById(update)) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    @RequestMapping("/asset/put")
    public BaseResponse addAsset(@ModelAttribute("request") @Validated AssetAddRequest request) {

        if (request.getAmount() == null
                || request.getType() == null) {
            return ResponseUtil.paramError();
        }
        Asset asset = new Asset();
        asset.setAmount(request.getAmount());
        asset.setType(request.getType());
        asset.setUserId(request.getUserId());
        if (request.getAvailable() != null) {
            asset.setAvailable(request.getAvailable() ? EnumYesOrNo.YES.getCode() : EnumYesOrNo.NO.getCode());
        }

        if (StringUtils.isNotEmpty(request.getName())) {
            asset.setExtName(request.getName());
        }
        if (assetService.addAsset(asset) > 0) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }


    @RequestMapping("/asset/delete")
    public BaseResponse deleteAsset(@ModelAttribute("request") @Validated AssetDeleteRequest request) {
        if (request.getAssetId() == null) {
            return ResponseUtil.paramError();
        }
        if (assetService.deleteAsset(request.getAssetId())) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    private void calAssetAfterMonth(AssetManageDTO assetManageDTO) {
        Long totalAsset = assetManageDTO.getTotalAsset();
        if (CollectionUtils.isNotEmpty(assetManageDTO.getMonthLiabilityModels())) {
            for (MonthLiabilityModel model : assetManageDTO.getMonthLiabilityModels()) {
                totalAsset -= model.getTotal();
                model.setAssetAfterThisMonth(totalAsset);
            }
        }
    }

}
