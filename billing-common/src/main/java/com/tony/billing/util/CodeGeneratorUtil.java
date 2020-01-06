package com.tony.billing.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by tonyjiang on 16-8-12.
 * 生成唯一的编码用的工具类
 */
public class CodeGeneratorUtil {
    private final static AtomicInteger COUNTER = new AtomicInteger();

    private final static String ZEO_SEQ = "0000000000000000000000";

    /**
     * 获取指定长度的唯一编码
     *
     * @param size 指定长度
     * @return 唯一编码
     */
    public static String getCode(int size) {
        if (size < 14) {
            return mod(COUNTER.addAndGet(1), size);
        }
        StringBuilder code = new StringBuilder();
        if (COUNTER.get() == 999999999) {
            synchronized (COUNTER) {
                COUNTER.set(0);
            }
        }
        code.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))).append(
                mod(COUNTER.addAndGet(1), size - 14));
        return code.toString();
    }

    private static String mod(int value, int size) {
        if (("" + value).length() == size) {
            return "" + value;
        }

        if (("" + value).length() > size) {
            return ("" + value).substring(0, size);
        } else {
            return ZEO_SEQ.substring(0, size - ("" + value).length()) + value;
        }
    }
}
