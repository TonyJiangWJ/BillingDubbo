package com.tony.billing.service.impl;

import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.dao.mapper.OwnershipCheckMapper;
import com.tony.billing.service.api.CommonValidateService;
import com.tony.billing.util.RedisUtils;
import com.tony.billing.util.UserIdContainer;
import org.springframework.beans.factory.annotation.Value;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author jiangwenjie 2019-03-20
 */
@Service
public class CommonValidateServiceImpl implements CommonValidateService {

    @Resource
    private OwnershipCheckMapper ownershipCheckMapper;
    @Resource
    private RedisUtils redisUtils;

    @Value("${ownership.check.hashKey:ownership_hash}")
    private String ownershipCheckHashKey;

    @Value("${ownership.check.validate:300}")
    private long ownershipCheckValidateTime;

    @Override
    public boolean validateOwnership(EnumOwnershipCheckTables table, Object primaryId) {
        String fieldKey = table.getTableName() + table.getPrimaryKey() + primaryId + table.getUserIdentifyKey() + UserIdContainer.getUserId();
        Optional<Boolean> optionalBoolean = redisUtils.hGetEx(ownershipCheckHashKey, fieldKey, Boolean.class);
        if (optionalBoolean.isPresent()) {
            return optionalBoolean.get();
        }
        boolean result = ownershipCheckMapper.countByPrimaryKeyAndUserId(table.getTableName(), table.getPrimaryKey(),
                primaryId, table.getUserIdentifyKey(), UserIdContainer.getUserId()) > 0;
        redisUtils.hSetEx(ownershipCheckHashKey, fieldKey, result, ownershipCheckValidateTime);
        return result;
    }
}
