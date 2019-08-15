package com.tony.billing.model;

import com.tony.billing.entity.AssetTypes;

/**
 * @author TonyJiang 2018/6/21
 */
public class AssetTypeModel {
    private String typeCode;
    private String typeDesc;
    private Long id;

    public AssetTypeModel() {
    }

    public AssetTypeModel(AssetTypes assetTypes) {
        this.typeCode = assetTypes.getTypeCode();
        this.id = assetTypes.getId();
        this.typeDesc = assetTypes.getTypeDesc();
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
