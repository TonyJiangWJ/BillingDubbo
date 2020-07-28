package com.tony.billing.util;

import com.tony.billing.constants.timing.TimeConstants;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

import static com.tony.billing.constants.timing.TimeConstants.DATE_PATTERN;
import static com.tony.billing.constants.timing.TimeConstants.FULL_TIME_PATTERN;

/**
 * @author jiangwenjie 2020/7/17
 */
public class DateUtil {

    private static boolean inValidDateStr(String dateStr, String pattern) {
        if (StringUtils.isNotEmpty(dateStr) && StringUtils.isNotEmpty(pattern)) {
            return dateStr.length() != pattern.length();
        }
        return true;
    }

    public static Date getDateFromString(String dateString, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateFromString(String dateStr) {
        return getDateFromString(dateStr, FULL_TIME_PATTERN);
    }

    public static LocalDateTime getLocalDateTimeFromStr(String dateStr, String pattern) {
        if (inValidDateStr(dateStr, pattern)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINA);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static LocalDateTime getLocalDateTimeFromStr(String dateStr) {
        return getLocalDateTimeFromStr(dateStr, FULL_TIME_PATTERN);
    }

    public static LocalDate getLocalDateFromStr(String dateStr, String pattern) {
        if (inValidDateStr(dateStr, pattern)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINA);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDate getLocalDateFromStr(String dateStr) {
        return getLocalDateFromStr(dateStr, DATE_PATTERN);
    }

    public static LocalDate getLastWorkDay(String dateStr) {
        LocalDate localDate = getLocalDateFromStr(dateStr, DATE_PATTERN);
        if (localDate == null) {
            return null;
        }
        DayOfWeek currentDayOfWeek = localDate.getDayOfWeek();
        int subDays = 0;
        switch (currentDayOfWeek) {
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
                subDays = 1;
                break;
            case MONDAY:
                subDays = 3;
                break;
            case SUNDAY:
                subDays = 2;
                break;
            default:
                throw new IllegalStateException("this should never happen");
        }
        return localDate.plusDays(-subDays);
    }

    public static String formatDateTime(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.toInstant().atZone(TimeConstants.CHINA_ZONE).format(formatter);
    }

    public static String formatDateTime(TemporalAccessor date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (date instanceof LocalDate) {
            date = ((LocalDate)date).atStartOfDay();
        }
        return formatter.format(date);
    }

    public static String formatDateTime(TemporalAccessor date) {
        return formatDateTime(date, FULL_TIME_PATTERN);
    }

    public static String formatDateTime(Date date) {
        return formatDateTime(date, FULL_TIME_PATTERN);
    }

    public static String formatDay(TemporalAccessor date) {
        return formatDateTime(date, DATE_PATTERN);
    }

    public static String formatDay(Date date) {
        return formatDateTime(date, DATE_PATTERN);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDateFromString("2020-07-11", DATE_PATTERN));
        System.out.println(formatDateTime(getLocalDateFromStr("2020-07-11"), DATE_PATTERN));
        System.out.println(formatDateTime(getLocalDateFromStr("2020-07-12"), DATE_PATTERN));
        System.out.println(formatDateTime(getLocalDateTimeFromStr("2020-07-11 12:00:00")));
        System.out.println(formatDateTime(new Date(), DATE_PATTERN));
        System.out.println(formatDay(getLastWorkDay("2020-07-11")));


//        System.out.println(formatDay(getDateFromString("2020-07-11", "yyyy-MM-dd")));
    }
}
