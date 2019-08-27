package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.Asset;

import java.util.List;
import java.util.Map;

public interface AssetMapper extends AbstractMapper<Asset> {

    List<Asset> page(Map params);

}
