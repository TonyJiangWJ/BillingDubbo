package com.tony.billing.controller;

import com.tony.billing.dto.LiabilityDTO;
import com.tony.billing.entity.AssetTypes;
import com.tony.billing.entity.Liability;
import com.tony.billing.request.liability.LiabilityAddRequest;
import com.tony.billing.request.liability.LiabilityDetailRequest;
import com.tony.billing.request.liability.LiabilityUpdateRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.response.liability.LiabilityDetailResponse;
import com.tony.billing.service.api.AssetTypesService;
import com.tony.billing.service.api.LiabilityService;
import com.tony.billing.util.ResponseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @author jiangwj20966 2018/2/22
 */
@RestController
@RequestMapping("/bootDemo")
public class LiabilityController extends BaseController {

    @Reference
    private LiabilityService liabilityService;

    @Reference
    private AssetTypesService assetTypesService;

    @RequestMapping("/liability/detail/get")
    public LiabilityDetailResponse getLiabilityInfo(@ModelAttribute("request") @Validated LiabilityDetailRequest request) {
        Liability liability = liabilityService.getLiabilityInfoById(request.getId());
        if (liability != null && liability.getUserId().equals(request.getUserId())) {
            LiabilityDetailResponse response = ResponseUtil.success(new LiabilityDetailResponse());

            response.setLiability(fillDTOWithType(liability));
            return response;
        }
        return ResponseUtil.dataNotExisting(new LiabilityDetailResponse());
    }

    /**
     * 设置type
     *
     * @param liability
     * @return
     */
    private LiabilityDTO fillDTOWithType(Liability liability) {
        LiabilityDTO liabilityDTO = new LiabilityDTO(liability);
        AssetTypes assetTypes = assetTypesService.getById(liability.getType());
        if (assetTypes != null) {
            liabilityDTO.setType(assetTypes.getTypeDesc());
        }
        return liabilityDTO;
    }

    @RequestMapping(value = "/liability/put", method = RequestMethod.POST)
    public BaseResponse addLiability(@ModelAttribute("request") @Validated LiabilityAddRequest request) {

        Liability liability = new Liability();
        liability.setRepaymentDay(request.getRepaymentDay());
        liability.setType(request.getType());
        liability.setAmount(request.getAmount());
        liability.setInstallment(request.getInstallment());
        liability.setUserId(request.getUserId());
        try {
            if (liabilityService.createLiabilityInfo(liability)) {
                return ResponseUtil.success();
            } else {
                return ResponseUtil.error();
            }
        } catch (SQLException e) {
            logger.error("create liability info error:", e);
            return ResponseUtil.sysError();
        }
    }

    @RequestMapping("/liability/update")
    public BaseResponse updateLiability(@ModelAttribute("request") @Validated LiabilityUpdateRequest request) {
        Liability update = new Liability();
        update.setId(request.getId());
        update.setAmount(request.getAmount());
        update.setPaid(request.getPaid());
        update.setUserId(request.getUserId());
        update.setVersion(request.getVersion());
        if (liabilityService.modifyLiabilityInfoById(update)) {
            return ResponseUtil.success();
        }
        return ResponseUtil.error();
    }
}
