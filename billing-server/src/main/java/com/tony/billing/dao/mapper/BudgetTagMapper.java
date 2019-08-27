package com.tony.billing.dao.mapper;

import com.tony.billing.entity.TagBudgetRef;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-21
 */
@Repository
public interface BudgetTagMapper {

    @Select("SELECT * FROM t_budget_tag WHERE budgetId=#{budgetId} AND tagId=#{tagId} AND isDeleted=0")
    List<TagBudgetRef> selectByBudgetIdAndTagId(@Param("budgetId") Long budgetId, @Param("tagId") Long tagId);

    @Select("SELECT COUNT(1) FROM t_budget_tag WHERE budgetId=#{budgetId} AND tagId=#{tagId} AND isDeleted=0")
    Integer countByBudgetIdAndTagId(@Param("budgetId") Long budgetId, @Param("tagId") Long tagId);
}
