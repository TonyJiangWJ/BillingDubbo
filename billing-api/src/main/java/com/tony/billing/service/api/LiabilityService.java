package com.tony.billing.service.api;

import com.tony.billing.entity.Liability;
import com.tony.billing.model.LiabilityModel;
import com.tony.billing.model.MonthLiabilityModel;
import org.apache.dubbo.config.annotation.Reference;

import java.sql.SQLException;
import java.util.List;

public interface LiabilityService {


    /**
     * 获取总负债信息
     *
     * @param userId
     * @return
     */
    List<LiabilityModel> getLiabilityModelsByUserId(Long userId);

    /**
     * 获取每月分期还款信息
     *
     * @param userId
     * @return
     */
    List<MonthLiabilityModel> getMonthLiabilityModelsByUserId(Long userId);

    /**
     * 查看负债详情
     *
     * @param id
     * @return
     */
    Liability getLiabilityInfoById(Long id);

    /**
     * 修改负债信息
     *
     * @param liability
     * @return
     */
    boolean modifyLiabilityInfoById(Liability liability);

    /**
     * 创建负债信息
     *
     * @param liability
     * @return
     */
    @Reference(retries = 0)
    boolean createLiabilityInfo(Liability liability) throws SQLException;

}
