package com.tony.billing.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaEncrypt {

    /**
     * 传入文本内容，返回 SHA-256 串
     *
     * @param strText
     * @return
     */
    public static String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText 明文
     * @param strType 加密类型
     * @return
     */
    private static String SHA(final String strText, final String strType) {

        String strResult = null;

        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes("utf-8"));

                byte byteBuffer[] = messageDigest.digest();

                StringBuilder strHexString = new StringBuilder();

                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
