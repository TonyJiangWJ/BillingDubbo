package com.tony.billing.constants.timing;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author jiangwenjie 2020/1/6
 */
public interface TimeConstants {
    ZoneId CHINA_ZONE = ZoneId.of("GMT+8");
    ZoneOffset CHINA_ZONE_OFFSET = ZoneOffset.of("+8");
    String FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DATE_PATTERN = "yyyy-MM-dd";
}
