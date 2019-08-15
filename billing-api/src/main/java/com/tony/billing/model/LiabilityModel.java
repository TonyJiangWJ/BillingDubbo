package com.tony.billing.model;

import com.tony.billing.dto.LiabilityDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TonyJiang on 2018/2/12
 */
public class LiabilityModel {
    private String type;
    private Long total;
    private Long remain;
    private List<LiabilityDTO> liabilityList;

    public LiabilityModel() {
        total = 0L;
        liabilityList = new ArrayList<>();
    }

    public LiabilityModel(String type) {
        this.type = type;
        total = 0L;
        liabilityList = new ArrayList<>();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<LiabilityDTO> getLiabilityList() {
        return liabilityList;
    }

    public void setLiabilityList(List<LiabilityDTO> liabilityList) {
        this.liabilityList = liabilityList;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }
}
