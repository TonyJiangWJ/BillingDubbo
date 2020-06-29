package com.tony.billing.quartz;

import com.tony.billing.service.api.FundHistoryValueService;
import com.tony.billing.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author jiangwenjie 2020/6/28
 */
@Slf4j
@Component
public class FundHistoryValueUpdateScheduler {

    @Autowired
    private FundHistoryValueService fundHistoryValueService;

    @Autowired
    private RedisUtils redisUtils;

    private final String FUND_UPDATE_LOCK = "fundHistoryValueUpdateLock";
    private final String RUNNING_FLAG = "fundHistoryValueUpdateRunning";
    private final String FLAG_RUNNING = "RUNNING";
    private final String FLAG_STOP = "STOP";
    private final String LOCK_LOCKED = "LOCKED";

    @Scheduled(cron = "0 0/1 9-15 * * 1-5")
    public void execute() {
        Optional<String> runningFlag = redisUtils.get(RUNNING_FLAG, String.class);
        if (runningFlag.isPresent() && FLAG_STOP.equals(runningFlag.get())) {
            log.info("当前已标记为停止执行，不执行更新");
            return;
        }
        Optional<String> lockInfo = redisUtils.get(FUND_UPDATE_LOCK, String.class);
        if (lockInfo.isPresent()) {
            log.info("基金更新正在进行，跳过调度");
            return;
        }
        log.info("开始执行更新基金数据");
        redisUtils.set(FUND_UPDATE_LOCK, LOCK_LOCKED, 180);
        fundHistoryValueService.updateFundHistoryValues();
    }

    @Scheduled(cron = "0 45 8 * * 1-5")
    public void setStart() {
        log.info("标记基金更新任务可以运行");
        redisUtils.set(RUNNING_FLAG, FLAG_RUNNING);
    }

    @Scheduled(cron = "0 15 15 * * 1-5")
    public void setStop() {
        log.info("标记基金更新任务停止运行");
        // 持续一小时，使得定时任务在三点15分开始停止执行
        redisUtils.set(RUNNING_FLAG, FLAG_STOP, 3600);
    }
}
