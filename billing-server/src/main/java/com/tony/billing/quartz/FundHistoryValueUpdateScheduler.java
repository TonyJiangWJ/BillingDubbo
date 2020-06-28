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

    @Scheduled(cron = "0 0/1 9-15 * * 1-5")
    public void execute() {
        Optional<String> lockInfo = redisUtils.get(FUND_UPDATE_LOCK, String.class);
        if (lockInfo.isPresent()) {
            log.info("基金更新正在进行，跳过调度");
            return;
        }
        log.info("开始执行更新基金数据");
        redisUtils.set(FUND_UPDATE_LOCK, "LOCKED", 180);
        fundHistoryValueService.updateFundHistoryValues();
    }
}
