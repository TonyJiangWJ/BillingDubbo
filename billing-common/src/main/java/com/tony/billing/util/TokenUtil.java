package com.tony.billing.util;

/**
 * @author by TonyJiang on 2017/7/2.
 */
public class TokenUtil {
    public static String getToken(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append(param);
        }
        return Md5Util.md5(sb.toString()) + Math.round(System.currentTimeMillis() / 1000f);
    }
}
