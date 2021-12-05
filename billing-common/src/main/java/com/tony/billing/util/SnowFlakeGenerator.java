package com.tony.billing.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 雪花算法发号器，存在问题：1，时钟回拨、机器上限、
 * @author jiangwenjie 2019-04-19
 */
@Component
public class SnowFlakeGenerator {

    private final static Pattern TIME_CONFIG_CHECKER = Pattern.compile("^\\d{14}$");
    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 10;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTAMP_LEFT = MACHINE_LEFT + MACHINE_BIT;

    /**
     * 机器标识
     */
    private long machineId;
    /**
     * 序列号
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private long lastTamp = -1L;
    /**
     * 起始的时间戳
     */
    private long startStamp;


    @Autowired
    private SnowFlakeGenerator(@Value("${snow.flake.machine.id:1}") long machineId,
                               @Value("${snow.flake.startStamp.datetime:20201201000000}") String startDatetime) {

        if (!TIME_CONFIG_CHECKER.matcher(startDatetime).find()) {
            throw new IllegalArgumentException("起始日期配置错误, 格式不正确");
        }

        this.startStamp = LocalDateTime.parse(startDatetime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).atZone(ZoneId.systemDefault()).toEpochSecond();
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("机器编码不能大于" + MAX_MACHINE_NUM + "或者小于零");
        }
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastTamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastTamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTamp = currStamp;
        // 时间戳部分
        return (currStamp - startStamp) << TIMESTAMP_LEFT
                // 机器标识部分
                | machineId << MACHINE_LEFT
                // 序列号部分
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastTamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlakeGenerator snowFlake = new SnowFlakeGenerator(1023, "20201201000000");

        for (int i = 0; i < (1 << 12); i++) {
            System.out.println(snowFlake.nextId());
        }

    }
}
