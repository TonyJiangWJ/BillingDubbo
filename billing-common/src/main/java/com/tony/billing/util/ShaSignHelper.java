package com.tony.billing.util;

import org.apache.commons.lang3.StringUtils;

public class ShaSignHelper {

    public static String sign(String str, String appSecret) {
        if (StringUtils.isBlank(appSecret)) {
            return null;
        }
        return ShaEncrypt.SHA256(appSecret + str + appSecret);
    }
}
