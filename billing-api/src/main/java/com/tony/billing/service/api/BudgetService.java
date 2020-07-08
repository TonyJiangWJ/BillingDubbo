package com.tony.billing.service.api;

import com.tony.billing.dto.BudgetDTO;
import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.entity.Budget;
import com.tony.billing.model.BudgetReportModel;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @author by TonyJiang on 2017/7/5.
 */
public interface BudgetService {
    /**
     * 创建预算信息
     * @param budget
     * @return
     */
    @Reference(retries = 0)
    Long insert(Budget budget);

    /**
     * 根据年月查询预算信息
     * @param budget belongYear belongMonth
     * @return
     */
    List<BudgetDTO> queryBudgetsByCondition(Budget budget);

    /**
     * 更新预算信息
     * @param budget
     * @return
     */
    boolean updateBudget(Budget budget);

    /**
     * 删除预算信息
     * @param budgetId
     * @return
     */
    boolean deleteBudget(Long budgetId);

    Budget getById(Long id);


    List<TagInfoDTO> getTagInfosByBudgetId(Long id, Long userId);

    BudgetReportModel getBudgetReportByMonth(String monthInfo, Long userId);

    /**
     * 获取最近6个月的预算概述信息
     * @return
     */
    @Reference(timeout = 10000)
    List<BudgetReportModel> getNearlySixMonth();
}
