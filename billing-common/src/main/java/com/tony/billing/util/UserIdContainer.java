package com.tony.billing.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangwj20966 7/17/2018
 */
@Slf4j
public class UserIdContainer {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();


    public static boolean isSet() {
        return USER_ID.get() != null;
    }

    public static Long getUserId() {
        Long userId = USER_ID.get();
        Preconditions.checkNotNull(userId);
        return userId;
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static void removeUserId() {
        USER_ID.remove();
    }


}
