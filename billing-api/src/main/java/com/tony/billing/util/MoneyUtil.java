package com.tony.billing.util;

import org.springframework.util.StringUtils;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public class MoneyUtil {

    public static String fen2Yuan(Long money) {
        if (money == null) {
            return null;
        }
        String s = money + "";
        if (s.length() < 2) {
            s = "0.0" + s;
        } else if (s.length() == 2) {
            s = "0." + s;
        } else {
            s = s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2);
        }
        return s;
    }

    public static Long yuan2fen(String money) {
        if (StringUtils.isEmpty(money)) {
            return null;
        }
        int idxDot = money.indexOf('.');
        int len = money.length();
        if (idxDot >= 0) {
            if ((len - idxDot) > 3) {
                return Long.valueOf(money.substring(0, idxDot + 3).replace(".", ""));
            } else if ((len - idxDot) == 3) {
                return Long.valueOf(money.replace(".", ""));
            } else {
                return Long.valueOf(money.replace(".", "") + "0");
            }
        } else {
            return Long.valueOf(money + "00");
        }
    }

    public static void main(String[] args) {
        System.out.println(yuan2fen("48.80877"));
    }
}
