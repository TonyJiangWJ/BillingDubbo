package com.tony;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * @author jiangwenjie 2020/1/6
 */
@Slf4j
@RunWith(JUnit4.class)
public class SampleTest {

    @Test
    public void testDayBetween() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = LocalDateTime.parse("2019-11-12 12:11:11", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("day between: {}", Duration.between(now, before).toDays());
        log.info("day between: {}", Period.between(now.toLocalDate(), before.toLocalDate()).getYears());

        log.info("date from '2019-01' : {}", LocalDate.parse("2019-01", new DateTimeFormatterBuilder().appendPattern("yyyy-MM").parseDefaulting(ChronoField.DAY_OF_MONTH, 1).toFormatter()));
    }
}
