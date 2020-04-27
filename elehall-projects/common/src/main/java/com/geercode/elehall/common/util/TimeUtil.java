package com.geercode.elehall.common.util;

import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public final class TimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");

    private TimeUtil() {
    }

    public static String now() {
        ZonedDateTime now = ZonedDateTime.of(LocalDateTime.now(), ZONE_SHANGHAI);
        return DATE_TIME_FORMATTER.format(now);
    }

    public static String format(String pattern, Date date) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String format(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    @SneakyThrows
    public static Date parse(String pattern, String date) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(date);
    }

    @SneakyThrows
    public static Date parse(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(date);
    }

    public static Date nextDays(Date date, int dayNum) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, dayNum);
        return c.getTime();
    }

    public static Date nextDays(int dayNum) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, dayNum);
        return c.getTime();
    }
}