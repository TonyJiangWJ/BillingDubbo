package com.tony.billing.dto;

import com.tony.billing.dto.base.VersionedDTO;
import com.tony.billing.entity.Asset;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TonyJiang on 2018/2/12
 */
public class AssetDTO extends VersionedDTO {
    private String name;
    private String type;
    private String available;
    private Long id;
    private Long amount;

    public AssetDTO() {

    }

    public AssetDTO(Asset asset) {
        if (asset != null) {
            this.amount = asset.getAmount();
            this.id = asset.getId();
            this.name = StringUtils.isNotEmpty(asset.getExtName()) ? asset.getExtName() : asset.getName();
            this.available = asset.getAvailable();
            this.setVersion(asset.getVersion());
        }
    }

    public AssetDTO(Asset asset, String typeDesc) {
        if (asset != null) {
            this.amount = asset.getAmount();
            this.id = asset.getId();
            this.name = StringUtils.isNotEmpty(asset.getExtName()) ? asset.getExtName() : asset.getName();
            this.available = asset.getAvailable();
            this.type = typeDesc;
            this.setVersion(asset.getVersion());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
