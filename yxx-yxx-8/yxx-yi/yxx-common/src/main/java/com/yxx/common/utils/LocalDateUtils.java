package com.yxx.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 基于Java 8时间API的时间工具类
 *
 * @author yxx
 */
public class LocalDateUtils {

    public final static String YYYY = "yyyy";
    public final static String YYYY_MM = YYYY + "-MM";
    public final static String YYYY_MM_DD = YYYY_MM + "-dd";
    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private final static List<String> PARSE_PATTERNS = Arrays.asList(
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    );

    /**
     * 获取当前LocalDate日期
     */
    public static LocalDate getNowLocalDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前LocalDateTime日期时间
     */
    public static LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期字符串（默认yyyy-MM-dd）
     */
    public static String getDate() {
        return formatLocalDate(LocalDate.now(), YYYY_MM_DD);
    }

    /**
     * 获取当前时间字符串（默认yyyy-MM-dd HH:mm:ss）
     */
    public static String getTime() {
        return formatLocalDateTime(LocalDateTime.now(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前时间戳字符串（默认yyyyMMddHHmmss）
     */
    public static String dateTimeNow() {
        return formatLocalDateTime(LocalDateTime.now(), YYYYMMDDHHMMSS);
    }

    /**
     * 格式化LocalDate日期
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化LocalDateTime日期时间
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串为LocalDate
     */
    public static LocalDate parseToLocalDate(Object dateStr) {
        if (Objects.isNull(dateStr)) {
            return null;
        }
        for (String pattern : PARSE_PATTERNS) {
            try {
                return LocalDate.parse(dateStr.toString(), DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {
                // 尝试下一种格式
            }
        }
        throw new IllegalArgumentException("无法解析日期字符串: " + dateStr.toString());
    }

    /**
     * 解析日期时间字符串为LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(Object dateTimeStr) {
        if (Objects.isNull(dateTimeStr)) {
            return null;
        }
        for (String pattern : PARSE_PATTERNS) {
            try {
                return LocalDateTime.parse(dateTimeStr.toString(), DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {
                // 尝试下一种格式
            }
        }
        throw new IllegalArgumentException("无法解析日期时间字符串: " + dateTimeStr.toString());
    }

    /**
     * 日期路径（年/月/日 如2023/08/12）
     */
    public static String datePath() {
        return formatLocalDate(LocalDate.now(), "yyyy/MM/dd");
    }

    /**
     * 日期路径（年月日 如20230812）
     */
    public static String dateTime() {
        return formatLocalDate(LocalDate.now(), "yyyyMMdd");
    }

    /**
     * 计算相差天数
     */
    public static int differentDays(LocalDate date1, LocalDate date2) {
        return (int) Math.abs(ChronoUnit.DAYS.between(date1, date2));
    }

    /**
     * 计算时间差（天/小时/分钟）
     */
    public static String timeDistance(LocalDateTime end, LocalDateTime start) {
        Duration duration = Duration.between(start, end);
        long days = Math.abs(duration.toDays());
        long hours = Math.abs(duration.toHours() % 24);
        long minutes = Math.abs(duration.toMinutes() % 60);
        return days + "天" + hours + "小时" + minutes + "分钟";
    }

    /**
     * 增加 Date ==> LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 增加 Date ==> LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}