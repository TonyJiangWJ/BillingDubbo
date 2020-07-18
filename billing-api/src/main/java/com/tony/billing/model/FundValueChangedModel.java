package com.tony.billing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangwenjie 2020/6/29
 */
@Data
@ToString
@EqualsAndHashCode
public class FundValueChangedModel implements Serializable {
    private Long id;
    private String fundCode;
    private String fundName;
    private Integer version;
    /**
     * 买入时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseDate;
    /**
     * 买入确认时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseConfirmDate;
    /**
     * 买入确认净值
     */
    private String fundPurchaseValue;
    /**
     * 买入份额
     */
    private String purchaseAmount;
    /**
     * 买入支出
     */
    private String purchaseCost;
    /**
     * 买入手续费
     */
    private String purchaseFee;
    /**
     * 最新净值确认时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date valueConfirmDate;
    /**
     * 最新确认净值
     */
    private String fundConfirmedValue;
    /**
     * 确认增长值
     */
    private String confirmedIncrease;
    /**
     * 确认增长率
     */
    private String confirmedIncreaseRate;
    /**
     * 上一天增长
     */
    private String lastDayConfirmedIncrease;
    /**
     * 上一天增长率
     */
    private String lastDayConfirmedIncreaseRate;
    /**
     * 估算时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assessmentTime;
    /**
     * 估算净值
     */
    private String assessmentValue;
    /**
     * 估算总增长
     */
    private String assessmentIncrease;
    /**
     * 估算总增长率
     */
    private String assessmentIncreaseRate;
    /**
     * 当日估算增值
     */
    private String todayIncrease;
    /**
     * 当日估算增长率
     */
    private String todayIncreaseRate;
    /**
     * 当日确认增值
     */
    private String todayConfirmedIncrease;
    /**
     * 当日确认增长率
     */
    private String todayConfirmedIncreaseRate;
    /**
     * 当日确认总增值
     */
    private String todayActualIncrease;
    /**
     * 当日确认总长率
     */
    private String todayActualIncreaseRate;
}
