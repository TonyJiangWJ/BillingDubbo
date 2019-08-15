package com.tony.billing.dto;

import com.tony.billing.dto.base.VersionedDTO;
import com.tony.billing.entity.Liability;

import java.text.SimpleDateFormat;

/**
 * @author TonyJiang on 2018/2/12
 */
public class LiabilityDTO extends VersionedDTO {

    public LiabilityDTO() {

    }

    public LiabilityDTO(Liability liability) {
        if (liability != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            this.id = liability.getId();
            this.amount = liability.getAmount();
            this.paid = liability.getPaid();
            this.index = liability.getIndex();
            this.installment = liability.getInstallment();
            this.name = liability.getName();
            if (liability.getRepaymentDay() != null) {
                this.repaymentDay = simpleDateFormat.format(liability.getRepaymentDay());
            }
            this.status = liability.getStatus();
            this.setVersion(liability.getVersion());
        }
    }

    public LiabilityDTO(Liability liability, String typeDesc) {
        if (liability != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            this.id = liability.getId();
            this.amount = liability.getAmount();
            this.paid = liability.getPaid();
            this.index = liability.getIndex();
            this.installment = liability.getInstallment();
            this.name = liability.getName();
            if (liability.getRepaymentDay() != null) {
                this.repaymentDay = simpleDateFormat.format(liability.getRepaymentDay());
            }
            this.status = liability.getStatus();
            this.setVersion(liability.getVersion());
            this.type = typeDesc;
        }
    }

    private Long id;
    private String name;
    private String type;
    private Long amount;
    private Long paid;
    private Integer status;
    private Integer installment;
    private Integer index;
    private String repaymentDay;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(String repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }
}
