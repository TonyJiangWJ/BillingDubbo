package com.tony.billing.service.api;

import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.entity.TagBudgetRef;
import com.tony.billing.entity.TagCostRef;
import com.tony.billing.entity.TagInfo;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/15.
 */
public interface TagInfoService {
    List<TagInfo> listTagInfo(TagInfo tagInfo);

    @Reference(retries = 0)
    Long putTagInfo(TagInfo tagInfo);

    TagInfo findTagInfoByName(String tagName);

    List<TagInfo> listTagInfoByTradeNo(String tradeNo);

    TagInfo getTagInfoById(Long id);

    @Reference(retries = 0)
    Long insertTagCostRef(TagCostRef tagCostRef);

    Long deleteTagById(Long id);

    Long countTagUsage(Long id, Long userId);

    /**
     * 添加预算标签关联关系
     *
     * @param budgetRef
     * @return
     */
    @Reference(retries = 0)
    Long insertTagBudgetRef(TagBudgetRef budgetRef);

    /**
     * 获取预算关联标签列表
     *
     * @param budgetId
     * @return
     */
    List<TagInfo> listTagInfoByBudgetId(Long budgetId);

    Long delBudgetTag(Long tagId, Long budgetId);

    List<TagInfo> listAssignableTagsByBudgetId(Long budgetId);

    boolean deleteCostTag(Long costId, Long tagId);

    /**
     * 批量新增账单标签关联关系
     *
     * @param tagId 标签id
     * @param recordIds 账单id列表
     * @return
     */
    @Reference(retries = 0, timeout = 10000)
    boolean batchInsertTagCostRef(Long tagId, List<Long> recordIds);

    List<TagInfoDTO> listCommonTagInfos(List<Long> recordIds);

    boolean batchDeleteCostTags(Long tagId, List<Long> recordIds);
}
