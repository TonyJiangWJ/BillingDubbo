package com.tony.billing.dto;

import com.tony.billing.dto.base.VersionedDTO;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-22
 */
public class BudgetReportItemDTO extends VersionedDTO {
    private Long id;
    private String name;
    private Long amount;
    private Long used;
    private Long remain;

    private List<TagCostInfoDTO> tagInfos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public List<TagCostInfoDTO> getTagInfos() {
        return tagInfos;
    }

    public void setTagInfos(List<TagCostInfoDTO> tagInfos) {
        this.tagInfos = tagInfos;
    }
}
