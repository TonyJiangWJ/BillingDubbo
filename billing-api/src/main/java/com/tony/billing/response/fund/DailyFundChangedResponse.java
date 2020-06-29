package com.tony.billing.response.fund;

import com.tony.billing.model.FundValueChangedModel;
import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/29
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DailyFundChangedResponse extends BaseResponse {
    private List<FundValueChangedModel> fundDetailInfos;
    private List<FundValueChangedModel> summaryFundInfos;
    private String assessmentDate;
    private String totalCost;
    private String totalFee;
    private String totalHold;
    /**
     * 当日已确认增长数据
     */
    private String confirmedIncrease;
    private String confirmedIncreaseRate;
    /**
     * 估算总增长数据
     */
    private String assessmentIncrease;
    private String assessmentIncreaseRate;
    /**
     * 当日增长数据
     */
    private String todayIncrease;
    private String todayIncreaseRate;

    public void calculateIncreaseInfo() {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalConfirmedIncrease = BigDecimal.ZERO;
        BigDecimal totalTodayIncrease = BigDecimal.ZERO;
        BigDecimal totalAssessmentIncrease = BigDecimal.ZERO;
        BigDecimal totalFee = BigDecimal.ZERO;
        BigDecimal totalHold = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(summaryFundInfos)) {
            for (FundValueChangedModel changedModel : summaryFundInfos) {
                // 总持有成本
                totalCost = totalCost.add(new BigDecimal(changedModel.getPurchaseCost()));
                // 总持有金额
                if (StringUtils.isNotEmpty(changedModel.getFundConfirmedValue())) {
                    totalHold = totalHold.add(new BigDecimal(changedModel.getFundConfirmedValue()).multiply(new BigDecimal(changedModel.getPurchaseAmount())));
                } else {
                    // 未能获取估算净值，直接获取：支出-手续费
                    totalHold = totalHold.add(new BigDecimal(changedModel.getPurchaseCost()).subtract(new BigDecimal(changedModel.getPurchaseFee())));
                }
                // 总手续费
                totalFee = totalFee.add(new BigDecimal(changedModel.getPurchaseFee()));
                if (StringUtils.isNotEmpty(changedModel.getConfirmedIncrease())) {
                    // 确认总增加
                    totalConfirmedIncrease = totalConfirmedIncrease.add(new BigDecimal(changedModel.getConfirmedIncrease()));
                }
                if (StringUtils.isNotEmpty(changedModel.getAssessmentIncrease())) {
                    // 估算总增加
                    totalAssessmentIncrease = totalAssessmentIncrease.add(new BigDecimal(changedModel.getAssessmentIncrease()));
                }
                if (StringUtils.isNotEmpty(changedModel.getTodayIncrease())) {
                    // 当日总增加
                    totalTodayIncrease = totalTodayIncrease.add(new BigDecimal(changedModel.getTodayIncrease()));
                }
            }
        }
        this.totalCost = totalCost.toString();
        this.totalFee = totalFee.toString();
        this.totalHold = totalHold.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        this.confirmedIncrease = totalConfirmedIncrease.toString();
        this.confirmedIncreaseRate = calRate(totalConfirmedIncrease, totalCost).toString();
        this.todayIncrease = totalTodayIncrease.toString();
        this.todayIncreaseRate = calRate(totalTodayIncrease, totalCost).toString();
        this.assessmentIncrease = totalAssessmentIncrease.toString();
        this.assessmentIncreaseRate = calRate(totalAssessmentIncrease, totalCost).toString();
    }

    private BigDecimal calRate(BigDecimal increasedValue, BigDecimal oldVal) {
        if (oldVal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return increasedValue.multiply(BigDecimal.valueOf(100)).divide(oldVal, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
