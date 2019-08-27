package com.tony.billing.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author by TonyJiang on 2017/7/1.
 */
public class Md5Util {
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return str;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                stringBuilder.append("0").append(
                        Integer.toHexString(0xFF & b));
            } else {
                stringBuilder.append(Integer.toHexString(0xFF & b));
            }
        }
        return stringBuilder.toString();
    }

    public static String md5Sign(String priKey, Map<String, Object> paramMap) {
        if (paramMap.isEmpty()) {
            return null;
        }
        List<String> keys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(paramMap.get(key)).append("&");
        }
        sb.append(priKey);
        return md5(sb.toString());
    }
}
