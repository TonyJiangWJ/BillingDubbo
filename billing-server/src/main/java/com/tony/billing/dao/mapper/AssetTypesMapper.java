package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.AssetTypes;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author TonyJiang 2018/6/21
 */
@Repository
public interface AssetTypesMapper extends AbstractMapper<AssetTypes> {


    AssetTypes getByTypeCode(@Param("typeCode") String typeCode, @Param("userId") Long userId);

}
