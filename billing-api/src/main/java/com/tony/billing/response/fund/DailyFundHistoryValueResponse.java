package com.tony.billing.response.fund;

import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwenjie 2020/6/28
 */
@Data
@Slf4j
@ToString(callSuper = true)
public class DailyFundHistoryValueResponse extends BaseResponse {
    private String assessmentDate;
    private Integer length;
    private Map<String, List<String>> increaseRateMapping;
    private Map<String, String> fundInfoMap;

    public void calMaxLength() {
        if (CollectionUtils.isNotEmptyMap(increaseRateMapping)) {
            length = Integer.MIN_VALUE;
            for (Map.Entry<String, List<String>> entry : increaseRateMapping.entrySet()) {
                if (length < entry.getValue().size()) {
                    length = entry.getValue().size();
                }
            }
            log.info("计算得到最大长度：{}", length);
        } else {
            length = 0;
        }
    }

    public void reverseRateList() {
        if (CollectionUtils.isNotEmptyMap(increaseRateMapping)) {
            increaseRateMapping.values().forEach(increaseRateList -> {
                if (increaseRateList.size() < length) {
                    for (int i = 0; i < length - increaseRateList.size(); i++) {
                        increaseRateList.add("0");
                    }
                }
                // 反向排序
                Collections.reverse(increaseRateList);
            });
        }
    }

    /**
     * 反转fundCode和fundName 便于ECharts展示
     */
    public void reverseFundCodeAndName() {
        if (CollectionUtils.isNotEmptyMap(increaseRateMapping)) {
            Map<String, List<String>> newIncreaseRateMap = new HashMap<>(increaseRateMapping.size());
            for (Map.Entry<String, List<String>> entry : increaseRateMapping.entrySet()) {
                newIncreaseRateMap.put(fundInfoMap.get(entry.getKey()), entry.getValue());
            }
            increaseRateMapping = newIncreaseRateMap;
        }
    }
}
