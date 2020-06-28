package com.tony.billing.response.fund;

import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.ToString;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwenjie 2020/6/28
 */
@Data
@ToString(callSuper = true)
public class DailyFundHistoryValueResponse extends BaseResponse {
    private String confirmDate;
    private Integer length;
    private Map<String, List<String>> increaseRateMapping;
    private Map<String, String> fundInfoMap;

    public void checkMinLength() {
        if (CollectionUtils.isNotEmptyMap(increaseRateMapping)) {
            length = Integer.MAX_VALUE;
            for (Map.Entry<String, List<String>> entry : increaseRateMapping.entrySet()) {
                if (length > entry.getValue().size()) {
                    length = entry.getValue().size();
                }
            }
        } else {
            length = 0;
        }
    }

    /**
     * 反转fundCode和fundName 便于ECharts展示
     */
    public void revertFundCodeAndName() {
        if (CollectionUtils.isNotEmptyMap(increaseRateMapping)) {
            Map<String, List<String>> newIncreaseRateMap = new HashMap<>(increaseRateMapping.size());
            for (Map.Entry<String, List<String>> entry : increaseRateMapping.entrySet()) {
                newIncreaseRateMap.put(fundInfoMap.get(entry.getKey()), entry.getValue());
            }
            increaseRateMapping = newIncreaseRateMap;
        }
    }
}
