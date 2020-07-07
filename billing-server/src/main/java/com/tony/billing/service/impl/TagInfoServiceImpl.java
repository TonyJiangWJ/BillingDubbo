package com.tony.billing.service.impl;

import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.dao.mapper.BudgetMapper;
import com.tony.billing.dao.mapper.BudgetTagMapper;
import com.tony.billing.dao.mapper.CostTagMapper;
import com.tony.billing.dao.mapper.TagInfoMapper;
import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.entity.Budget;
import com.tony.billing.entity.TagBudgetRef;
import com.tony.billing.entity.TagCostRef;
import com.tony.billing.entity.TagInfo;
import com.tony.billing.functions.TagInfoToDtoListSupplier;
import com.tony.billing.service.api.TagInfoService;
import com.tony.billing.service.api.CommonValidateService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author by TonyJiang on 2017/6/15.
 */
@Service
public class TagInfoServiceImpl extends AbstractServiceImpl<TagInfo, TagInfoMapper> implements TagInfoService {

    @Resource
    private BudgetMapper budgetMapper;
    @Resource
    private BudgetTagMapper budgetTagMapper;
    @Resource
    private CostTagMapper costTagMapper;
    @Resource
    private CommonValidateService commonValidateService;


    @Override
    public List<TagInfo> listTagInfo(TagInfo tagInfo) {
        return super.list(tagInfo);
    }

    @Override
    public Long putTagInfo(TagInfo tagInfo) {
        if (CollectionUtils.isNotEmpty(mapper.list(tagInfo))) {
            logger.error("同名标签已存在:{}", tagInfo.getTagName());
            return -1L;
        }
        return super.insert(tagInfo);
    }

    @Override
    public TagInfo findTagInfoByName(String tagName) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTagName(tagName);
        tagInfo.setUserId(UserIdContainer.getUserId());
        List<TagInfo> tagInfos = mapper.list(tagInfo);
        if (!CollectionUtils.isEmpty(tagInfos)) {
            return tagInfos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<TagInfo> listTagInfoByTradeNo(String tradeNo) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("tradeNo", tradeNo);
        params.put("userId", UserIdContainer.getUserId());
        Preconditions.checkNotNull(params.get("userId"));
        Preconditions.checkState(StringUtils.isNotEmpty(tradeNo));
        return mapper.listTagInfoByTradeNo(params);
    }

    @Override
    public TagInfo getTagInfoById(Long id) {
        return mapper.getTagInfoById(id);
    }

    @Override
    public Long insertTagCostRef(TagCostRef tagCostRef) {
        tagCostRef.setCreateTime(new Date());
        tagCostRef.setModifyTime(new Date());
        tagCostRef.setIsDeleted(EnumDeleted.NOT_DELETED.val());

        if (costTagMapper.countByCostIdAndTagId(tagCostRef.getCostId(), tagCostRef.getTagId()) == 0 &&
                mapper.insertTagCostRef(tagCostRef) > 0) {
            return tagCostRef.getId();
        }
        return -1L;
    }

    @Override
    public boolean deleteCostTag(Long costId, Long tagId) {
        return mapper.deleteCostTag(costId, tagId, new Date()) > 0;
    }

    @Override
    public Long deleteTagById(Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagId", id);
        param.put("modifyTime", new Date());
        mapper.deleteCostTagByTagId(param);
        return mapper.deleteTagById(param);
    }

    @Override
    public Long countTagUsage(Long id, Long userId) {
        return mapper.countTagUsage(id, userId);
    }

    @Override
    public Long insertTagBudgetRef(TagBudgetRef budgetRef) {
        Preconditions.checkNotNull(budgetRef.getBudgetId());
        Preconditions.checkNotNull(budgetRef.getTagId());
        budgetRef.setCreateTime(new Date());
        budgetRef.setModifyTime(new Date());
        if (budgetTagMapper.countByBudgetIdAndTagId(budgetRef.getBudgetId(), budgetRef.getTagId()) > 0) {
            logger.error("tag:{} 已经绑定到当前预算 budget:{}", budgetRef.getTagId(), budgetRef.getBudgetId());
            return -3L;
        }
        budgetRef.setIsDeleted(EnumDeleted.NOT_DELETED.val());
        TagInfo tagInfo = mapper.getTagInfoById(budgetRef.getTagId());
        Budget budget = budgetMapper.getById(budgetRef.getBudgetId(), UserIdContainer.getUserId());

        if (tagInfo != null && budget != null && tagInfo.getUserId().equals(UserIdContainer.getUserId())) {
            List<Long> boundTagsToThisMonth = mapper.listTagIdsByBudgetMonth(budget.getBelongYear(), budget.getBelongMonth(), UserIdContainer.getUserId(), budget.getId());
            if (CollectionUtils.isNotEmpty(boundTagsToThisMonth) && boundTagsToThisMonth.stream().anyMatch(tagId -> tagId.equals(tagInfo.getId()))) {
                logger.error("tag:{} 已经绑定到当月其他预算", tagInfo.getId());
                return -2L;
            }
            if (mapper.insertTagBudgetRef(budgetRef) > 0) {
                return budgetRef.getId();
            }
        }
        return -1L;
    }

    @Override
    public List<TagInfo> listTagInfoByBudgetId(Long budgetId) {
        return mapper.listTagInfoByBudgetId(budgetId, UserIdContainer.getUserId());
    }

    @Override
    public Long delBudgetTag(Long tagId, Long budgetId) {
        return mapper.deleteBudgetTag(tagId, budgetId, new Date());
    }

    @Override
    public List<TagInfo> listAssignableTagsByBudgetId(Long budgetId) {
        return mapper.listAssignableTagsByBudgetId(budgetId, UserIdContainer.getUserId());
    }

    @Override
    public boolean batchInsertTagCostRef(Long tagId, List<Long> recordIds) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(recordIds));
        recordIds = recordIds.stream().filter(costId -> {
                   boolean flag1 = commonValidateService.validateOwnership(EnumOwnershipCheckTables.COST_RECORD_WITH_ID, costId);
                   boolean flag2 = mapper.countByCostAndTag(costId, tagId) == 0;
                   return flag1 && flag2;
                }
        ).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(recordIds)) {
            return mapper.batchInsertTagBudgetRef(tagId, recordIds, new Date(), new Date()) > 0;
        }
        return false;
    }

    @Override
    public List<TagInfoDTO> listCommonTagInfos(List<Long> recordIds) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(recordIds));
        List<TagInfo> commonTags = mapper.listCommonTagInfos(recordIds, recordIds.size());
        return new TagInfoToDtoListSupplier(commonTags).get();
    }

    @Override
    public boolean batchDeleteCostTags(Long tagId, List<Long> recordIds) {
        Preconditions.checkState(CollectionUtils.isNotEmpty(recordIds));
        Preconditions.checkNotNull(tagId);
        return mapper.batchDeleteCostTag(tagId, recordIds) > 0;
    }
}
