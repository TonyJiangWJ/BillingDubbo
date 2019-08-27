package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.Budget;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface BudgetMapper extends AbstractMapper<Budget> {

    List<Budget> findByYearMonth(Budget budget);

}
