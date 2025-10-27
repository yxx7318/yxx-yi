package com.yxx.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimestampUtils {

    /**
     * 获取秒时间戳
     */
    public static Long secondTimestamp() {
        return Instant.now().getEpochSecond();
    }

    /**
     * LocalDate 转秒时间戳（使用系统默认时区）
     *
     * @param localDate LocalDate 对象
     * @return 秒时间戳
     */
    public static long localDateToSecondTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();
    }

    /**
     * LocalDateTime 转秒时间戳（使用系统默认时区）
     *
     * @param localDateTime LocalDateTime 对象
     * @return 秒时间戳
     */
    public static long localDateTimeToSecondTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toEpochSecond();
    }

    /**
     * Date 转秒时间戳
     *
     * @param date Date 对象
     * @return 秒时间戳
     */
    public static long dateToSecondTimestamp(Date date) {
        return date.toInstant().getEpochSecond();
    }

    /**
     * 获取毫秒时间戳
     */
    public static Long millisecondTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * LocalDate 转毫秒时间戳（使用系统默认时区）
     *
     * @param localDate LocalDate 对象
     * @return 毫秒时间戳
     */
    public static long localDateToMillisecondTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * LocalDateTime 转毫秒时间戳（使用系统默认时区）
     *
     * @param localDateTime LocalDateTime 对象
     * @return 毫秒时间戳
     */
    public static long localDateTimeToMillisecondTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * Date 转毫秒时间戳
     *
     * @param date Date 对象
     * @return 毫秒时间戳
     */
    public static long dateToMillisecondTimestamp(Date date) {
        return date.getTime();
    }

    /**
     * 秒时间戳转 Date
     *
     * @param secondTimestamp 秒时间戳
     * @return Date 对象
     */
    public static Date secondToDate(long secondTimestamp) {
        return Date.from(Instant.ofEpochSecond(secondTimestamp));
    }

    /**
     * 秒时间戳转 LocalDate（使用系统默认时区）
     *
     * @param secondTimestamp 秒时间戳
     * @return LocalDate 对象
     */
    public static LocalDate secondToLocalDate(long secondTimestamp) {
        return Instant.ofEpochSecond(secondTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 秒时间戳转 LocalDateTime（使用系统默认时区）
     *
     * @param secondTimestamp 秒时间戳
     * @return LocalDateTime 对象
     */
    public static LocalDateTime secondToLocalDateTime(long secondTimestamp) {
        return Instant.ofEpochSecond(secondTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 毫秒时间戳转 Date
     *
     * @param millisecondTimestamp 毫秒时间戳
     * @return Date 对象
     */
    public static Date millisecondToDate(long millisecondTimestamp) {
        return new Date(millisecondTimestamp);
    }

    /**
     * 毫秒时间戳转 LocalDate（使用系统默认时区）
     *
     * @param millisecondTimestamp 毫秒时间戳
     * @return LocalDate 对象
     */
    public static LocalDate millisecondToLocalDate(long millisecondTimestamp) {
        return Instant.ofEpochMilli(millisecondTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 毫秒时间戳转 LocalDateTime（使用系统默认时区）
     *
     * @param millisecondTimestamp 毫秒时间戳
     * @return LocalDateTime 对象
     */
    public static LocalDateTime millisecondToLocalDateTime(long millisecondTimestamp) {
        return Instant.ofEpochMilli(millisecondTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
