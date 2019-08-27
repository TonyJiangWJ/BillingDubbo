package com.tony.billing.dao.mapper;

import com.tony.billing.entity.TagCostRef;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-21
 */
@Repository
public interface CostTagMapper {

    @Select("SELECT * FROM t_cost_tag WHERE costId=#{costId} AND tagId=#{tagId} AND isDeleted=0")
    List<TagCostRef> selectByCostIdAndTagId(@Param("costId") Long costId, @Param("tagId") Long tagId);

    @Select("SELECT COUNT(1) FROM t_cost_tag WHERE costId=#{costId} AND tagId=#{tagId} AND isDeleted=0")
    Integer countByCostIdAndTagId(@Param("costId") Long costId, @Param("tagId") Long tagId);
}
