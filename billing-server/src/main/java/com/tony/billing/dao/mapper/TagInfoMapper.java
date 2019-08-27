package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.TagBudgetRef;
import com.tony.billing.entity.TagCostRef;
import com.tony.billing.entity.TagInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface TagInfoMapper extends AbstractMapper<TagInfo> {

    List<TagInfo> listTagInfoByTradeNo(Map param);

    TagInfo getTagInfoById(Long id);

    Long insertTagCostRef(TagCostRef tagCostRef);

    Long deleteCostTag(@Param("costId") Long costId, @Param("tagId") Long tagId, @Param("modifyTime") Date modifyTime);

    Long deleteTagById(Map param);

    Long deleteCostTagByTagId(Map param);

    List<TagInfo> queryTagByName(@Param("tagName") String tagName, @Param("userId") Long userId);

    Long countTagUsage(Long id);

    Long insertTagBudgetRef(TagBudgetRef tagBudgetRef);

    /**
     * 删除预算和标签关联关系
     *
     * @param tagId
     * @param budgetId
     * @param modifyTime
     * @return
     */
    Long deleteBudgetTag(@Param("tagId") Long tagId, @Param("budgetId") Long budgetId, @Param("modifyTime") Date modifyTime);

    /**
     * 获取预算关联的标签列表
     *
     * @param budgetId
     * @param userId
     * @return
     */
    List<TagInfo> listTagInfoByBudgetId(@Param("budgetId") Long budgetId, @Param("userId") Long userId);


    /**
     * 根据提供的月份信息获取关联到预算标签列表
     *
     * @param budgetYear     年份信息
     * @param budgetMonth    月份信息
     * @param exceptBudgetId nullable 排除的预算id
     * @param userId         用户id
     * @return
     */
    List<Long> listTagIdsByBudgetMonth(@Param("budgetYear") String budgetYear,
                                       @Param("budgetMonth") Integer budgetMonth,
                                       @Param("userId") Long userId,
                                       @Param("exceptBudgetId") Long exceptBudgetId);

    /**
     * 获取未关联到预算的标签
     *
     * @param budgetId 预算id
     * @param userId   用户id
     * @return 未关联的标签
     */
    List<TagInfo> listAssignableTagsByBudgetId(@Param("budgetId") Long budgetId, @Param("userId") Long userId);

    /**
     * 批量新增关联标签, 执行前需要校验数据有效性
     *
     * @param tagId
     * @param recordIds
     * @param createTime
     * @param modifyTime
     * @return
     */
    Integer batchInsertTagBudgetRef(@Param("tagId") Long tagId,
                                    @Param("recordIds") List<Long> recordIds,
                                    @Param("createTime") Date createTime,
                                    @Param("modifyTime") Date modifyTime);

    /**
     * 检查是否存在关联关系
     *
     * @param costId
     * @param tagId
     * @return
     */
    int countByCostAndTag(@Param("costId")Long costId, @Param("tagId")Long tagId);

    List<TagInfo> listCommonTagInfos(@Param("costIds") List<Long> costIds, @Param("size") Integer size);

    int batchDeleteCostTag(@Param("tagId") Long tagId, @Param("costIds") List<Long> costIds);
}
