package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.tony.billing.constants.enums.EnumLiabilityStatus;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dao.mapper.LiabilityMapper;
import com.tony.billing.dto.LiabilityDTO;
import com.tony.billing.entity.AssetTypes;
import com.tony.billing.entity.Liability;
import com.tony.billing.model.LiabilityModel;
import com.tony.billing.model.MonthLiabilityModel;
import com.tony.billing.service.api.AssetTypesService;
import com.tony.billing.service.api.LiabilityService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class LiabilityServiceImpl extends AbstractServiceImpl<Liability, LiabilityMapper> implements LiabilityService {

    @Resource
    private AssetTypesService assetTypesService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 获取总负债信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<LiabilityModel> getLiabilityModelsByUserId(Long userId) {
        assert userId != null;
        Liability query = new Liability();
        query.setUserId(userId);
        query.setStatus(0);
        List<Liability> liabilities = mapper.listLiabilityGroupByType(query);
        List<LiabilityModel> resultModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(liabilities)) {
            resultModels = liabilities.stream().collect(Collectors.groupingBy(liability -> {
                AssetTypes assetTypes = assetTypesService.getAssetTypeByIdWithCache(liability.getType());
                if (assetTypes != null && StringUtils.isNotEmpty(assetTypes.getParentCode())) {
                    return assetTypes.getParentCode();
                } else {
                    logger.error("未能获取负债主类别 liabilityId:{} assetTypes:{}", liability.getId(), JSON.toJSONString(assetTypes));
                    return "UNDEFINED";
                }
                // 根据父类型分组
            })).entrySet()
                    .stream()
                    // 分组后继续对每个组别进行统计
                    .map(stringListEntry -> {
                        String parentCode = stringListEntry.getKey();
                        AssetTypes parentType = assetTypesService.getAssetTypeByCodeWithCache(parentCode);
                        String assetTypeDesc = null;
                        if (parentType != null) {
                            assetTypeDesc = parentType.getTypeDesc();
                        } else {
                            assetTypeDesc = "未知类型";
                        }
                        LiabilityModel liabilityModel = new LiabilityModel();
                        // 获取负债信息类型子列表统计信息
                        List<Liability> subLiabilities = stringListEntry.getValue();

                        MultiSumContainer sumContainer = getMultiFieldSums(subLiabilities,
                                (container, liability) -> new MultiSumContainer(container.getTotal() + liability.getAmount(),
                                        container.getRemain() + liability.getAmount() - liability.getPaid())
                        );

                        liabilityModel.setType(assetTypeDesc);
                        liabilityModel.setTotal(sumContainer.getTotal());
                        liabilityModel.setRemain(sumContainer.getRemain());
                        // 将子列表信息转换成liabilityDTO 仅仅需要总金额、已还金额以及名称即可
                        liabilityModel.setLiabilityList(subLiabilities.stream().map(
                                subLiability -> {
                                    LiabilityDTO liabilityDTO = new LiabilityDTO();
                                    liabilityDTO.setName(subLiability.getName());
                                    liabilityDTO.setAmount(subLiability.getAmount());
                                    liabilityDTO.setPaid(subLiability.getPaid());
                                    return liabilityDTO;
                                }
                        ).collect(Collectors.toList()));
                        return liabilityModel;

                    }).collect(Collectors.toList());
        }

        return resultModels;
    }


    /**
     * 将负债信息列表根据负债类型分类整合
     *
     * @param liabilities 负债信息列表
     * @return
     */
    private List<LiabilityModel> getLiabilityModelsByLiabilityList(List<Liability> liabilities) {
        List<LiabilityModel> modelResultList = new ArrayList<>();
        Long userId = UserIdContainer.getUserId();
        if (CollectionUtils.isNotEmpty(liabilities)) {
            modelResultList = liabilities.parallelStream()
                    .collect(Collectors.groupingBy(Liability::getType))
                    .entrySet()
                    .parallelStream()
                    .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                    .map(entry -> {
                        Long assetTypesId = entry.getKey();
                        UserIdContainer.setUserId(userId);
                        AssetTypes type = assetTypesService.getAssetTypeByIdWithCache(assetTypesId);
                        LiabilityModel liabilityModel = new LiabilityModel();
                        liabilityModel.setType(type.getTypeDesc());
                        liabilityModel.setLiabilityList(
                                entry.getValue().stream().map(LiabilityDTO::new)
                                        .filter(liabilityDTO -> liabilityDTO.getId() != null)
                                        .collect(Collectors.toList())
                        );
                        MultiSumContainer sumContainer = getMultiFieldSums(
                                liabilityModel.getLiabilityList(),
                                (multiSumContainer, liabilityDTO) -> new MultiSumContainer(multiSumContainer.getTotal() + liabilityDTO.getAmount(),
                                        multiSumContainer.getRemain() + liabilityDTO.getAmount() - (liabilityDTO.getPaid() == null ? 0 : liabilityDTO.getPaid())
                                )
                        );
                        liabilityModel.setTotal(sumContainer.getTotal());
                        liabilityModel.setRemain(sumContainer.getRemain());
                        return liabilityModel;
                    }).collect(Collectors.toList());
        }
        return modelResultList;
    }

    @Override
    public List<MonthLiabilityModel> getMonthLiabilityModelsByUserId(Long userId) {
        Liability query = new Liability();
        query.setUserId(userId);
        query.setStatus(0);
        List<Liability> liabilities = super.list(query);
        Collections.sort(liabilities);
        List<MonthLiabilityModel> monthLiabilityModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(liabilities)) {
            monthLiabilityModels = liabilities.parallelStream()
                    .collect(Collectors.groupingBy(liability -> liability.getRepaymentDay().toInstant().atZone(TimeConstants.CHINA_ZONE).format(DateTimeFormatter.ofPattern("yyyy-MM"))))
                    .entrySet()
                    .parallelStream()
                    .filter(listEntry -> CollectionUtils.isNotEmpty(listEntry.getValue()))
                    .map(entry -> {
                        UserIdContainer.setUserId(userId);
                        MonthLiabilityModel monthLiabilityModel = new MonthLiabilityModel(entry.getKey());
                        monthLiabilityModel.setLiabilityModels(getLiabilityModelsByLiabilityList(entry.getValue()));
                        MultiSumContainer sumContainer = getMultiFieldSums(monthLiabilityModel.getLiabilityModels(),
                                (multiSumContainer, liabilityModel) -> new MultiSumContainer(multiSumContainer.getTotal() + liabilityModel.getTotal(),
                                        multiSumContainer.getRemain() + liabilityModel.getRemain()));
                        monthLiabilityModel.setTotal(sumContainer.getTotal());
                        monthLiabilityModel.setRemain(sumContainer.getRemain());
                        return monthLiabilityModel;
                    }).collect(Collectors.toList());
            monthLiabilityModels.sort(Comparator.comparing(MonthLiabilityModel::getMonth));
        }
        return monthLiabilityModels;
    }


    @Override
    public Liability getLiabilityInfoById(Long id) {
        return super.getById(id);
    }

    @Override
    public boolean modifyLiabilityInfoById(Liability liability) {
        if (liability.getAmount().equals(liability.getPaid())) {
            liability.setStatus(EnumLiabilityStatus.PAID.getStatus());
        }
        return super.update(liability);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createLiabilityInfo(Liability liability) throws SQLException {
        LocalDate repaymentDay = LocalDate.from(liability.getRepaymentDay().toInstant().atZone(TimeConstants.CHINA_ZONE));
        int installment = liability.getInstallment();

        AssetTypes assetTypes = assetTypesService.getAssetTypeByIdWithCache(liability.getType());
        if (assetTypes != null && (assetTypes.getUserId().equals(liability.getUserId()) || assetTypes.getUserId().equals(-1L))) {
            liability.setName(assetTypes.getTypeDesc());
        } else {
            logger.error("assetTypes不存在或者权限不足 id:{} userId:{}", liability.getType(), liability.getUserId());
            return false;
        }

        if (installment == 1) {
            liability.setIndex(1);
            return super.insert(liability) > 0;
        } else {
            Long totalAmount = liability.getAmount();
            Long perInstallmentAmount = totalAmount / installment;
            long overflow = 0L;
            if (totalAmount % installment != 0) {
                overflow = totalAmount - perInstallmentAmount * installment;
            }

            List<Liability> newLiabilities = new ArrayList<>();
            Liability newRecord = null;

            for (int i = 1; i <= installment; i++) {
                newRecord = new Liability();
                if (installment == i && overflow != 0) {
                    newRecord.setAmount(perInstallmentAmount + overflow);
                } else {
                    newRecord.setAmount(perInstallmentAmount);
                }
                newRecord.setIndex(i);
                newRecord.setInstallment(installment);
                newRecord.setName(liability.getName());
                newRecord.setRepaymentDay(Date.from(repaymentDay.atStartOfDay(TimeConstants.CHINA_ZONE).toInstant()));
                newRecord.setType(liability.getType());
                newRecord.setUserId(liability.getUserId());
                newLiabilities.add(newRecord);
                if (super.insert(newRecord) <= 0) {
                    throw new SQLException("error insert");
                }
                repaymentDay = repaymentDay.plusMonths(1);
            }
            logger.info("new Inserted:{}", JSON.toJSONString(newLiabilities));
        }
        return true;
    }


    private <T> MultiSumContainer getMultiFieldSums(List<T> originList, MultiSumContainerFunction<MultiSumContainer, T> accumulator) {
        return originList.stream().reduce(
                new MultiSumContainer(0, 0),
                // 累加器
                accumulator,
                // 组合, 串行操作不受影响
                (a, b) -> null
        );
    }

    private interface MultiSumContainerFunction<R extends MultiSumContainer, U> extends BiFunction<R, U, R> {
        @Override
        R apply(R r, U u);
    }


    private static class MultiSumContainer {
        private long total;
        private long remain;

        MultiSumContainer(long total, long remain) {
            this.total = total;
            this.remain = remain;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public long getRemain() {
            return remain;
        }

        public void setRemain(long remain) {
            this.remain = remain;
        }
    }
}
