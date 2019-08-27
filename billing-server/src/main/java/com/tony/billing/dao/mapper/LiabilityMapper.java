package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.Liability;

import java.util.List;
import java.util.Map;

public interface LiabilityMapper extends AbstractMapper<Liability> {

    List<Liability> page(Map params);

    List<Liability> listLiabilityGroupByType(Liability query);
}
