package com.yxx.common.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
     * 获取当前时间格式字符串（默认yyyyMMddHHmmss）
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
    public static LocalDate parseToLocalDate(Object dateStr, String pattern) {
        if (Objects.isNull(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr.toString(), DateTimeFormatter.ofPattern(pattern));
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
                return parseToLocalDate(dateStr.toString(), pattern);
            } catch (DateTimeParseException ignored) {
                // 尝试下一种格式
            }
        }
        throw new IllegalArgumentException("无法解析日期字符串: " + dateStr.toString());
    }

    /**
     * 解析日期字符串为LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(Object dateTimeStr, String pattern) {
        if (Objects.isNull(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr.toString(), DateTimeFormatter.ofPattern(pattern));
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
                return parseToLocalDateTime(dateTimeStr.toString(), pattern);
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

    /**
     * 添加时间
     */
    public static LocalDateTime addTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    /**
     * 减少时间
     */
    public static LocalDateTime minusTime(Duration duration) {
        return LocalDateTime.now().minus(duration);
    }

    /**
     * 是否在此之前
     */
    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    /**
     * 是否在此之后
     * @param date
     * @return
     */
    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    /**
     * 创建时间范围
     */
    public static LocalDateTime[] buildBetweenTime(int year1, int month1, int day1,
                                                   int year2, int month2, int day2) {
        return new LocalDateTime[]{buildTime(year1, month1, day1), buildTime(year2, month2, day2)};
    }

    /**
     * 判断指定时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      指定时间
     * @return 是否在范围内
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime, Timestamp time) {
        if (startTime == null || endTime == null || time == null) {
            return false;
        }

        // 将Timestamp转换为LocalDateTime
        LocalDateTime targetTime = time.toLocalDateTime();

        // 检查时间是否在范围内（包含边界）
        return (targetTime.isEqual(startTime) || targetTime.isAfter(startTime)) &&
                (targetTime.isEqual(endTime) || targetTime.isBefore(endTime));
    }

    /**
     * 判断指定时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      指定时间字符串
     * @return 是否在范围内
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime, String time) {
        if (startTime == null || endTime == null || time == null) {
            return false;
        }

        try {
            // 解析时间字符串
            LocalDateTime targetTime = parseToLocalDateTime(time);
            if (targetTime == null) {
                return false;
            }

            // 检查时间是否在范围内
            return (targetTime.isEqual(startTime) || targetTime.isAfter(startTime)) &&
                    (targetTime.isEqual(endTime) || targetTime.isBefore(endTime));
        } catch (DateTimeParseException e) {
            // 解析失败时返回false
            return false;
        }
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否在范围内
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 检查当前时间是否在范围内
        return (now.isEqual(startTime) || now.isAfter(startTime)) &&
                (now.isEqual(endTime) || now.isBefore(endTime));
    }

    /**
     * 判断当前日期是否在该日期范围内
     *
     * @param startDate 开始日期字符串
     * @param endDate   结束日期字符串
     * @return 是否在范围内
     */
    public static boolean isBetween(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }

        try {
            // 解析日期字符串
            LocalDate start = parseToLocalDate(startDate);
            LocalDate end = parseToLocalDate(endDate);
            LocalDate nowDate = LocalDate.now();
            if (start == null || end == null) {
                return false;
            }

            // 检查当前日期是否在范围内
            return (nowDate.isEqual(start) || nowDate.isAfter(start)) &&
                    (nowDate.isEqual(end) || nowDate.isBefore(end));
        } catch (DateTimeParseException e) {
            // 解析失败时返回false
            return false;
        }
    }

    /**
     * 判断时间段是否重叠
     *
     * @param startTime1 开始 time1
     * @param endTime1   结束 time1
     * @param startTime2 开始 time2
     * @param endTime2   结束 time2
     * @return 重叠：true 不重叠：false
     */
    public static boolean isOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        LocalDate nowDate = LocalDate.now();
        return LocalDateTimeUtil.isOverlap(LocalDateTime.of(nowDate, startTime1), LocalDateTime.of(nowDate, endTime1),
                LocalDateTime.of(nowDate, startTime2), LocalDateTime.of(nowDate, endTime2));
    }

    /**
     * 获取指定日期所在的月份的开始时间
     * 例如：2023-09-30 00:00:00,000
     *
     * @param date 日期
     * @return 月份的开始时间
     */
    public static LocalDateTime beginOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取指定日期所在的月份的最后时间
     * 例如：2023-09-30 23:59:59,999
     *
     * @param date 日期
     * @return 月份的结束时间
     */
    public static LocalDateTime endOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 获得指定日期所在季度
     *
     * @param date 日期
     * @return 所在季度
     */
    public static int getQuarterOfYear(LocalDateTime date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * 获取指定日期到现在过了几天，如果指定日期在当前日期之后，获取结果为负
     *
     * @param dateTime 日期
     * @return 相差天数
     */
    public static Long between(LocalDateTime dateTime) {
        return LocalDateTimeUtil.between(dateTime, LocalDateTime.now(), ChronoUnit.DAYS);
    }

    /**
     * 获取今天的开始时间
     *
     * @return 今天
     */
    public static LocalDateTime getToday() {
        return LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
    }

    /**
     * 获取昨天的开始时间
     *
     * @return 昨天
     */
    public static LocalDateTime getYesterday() {
        return LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(1));
    }

    /**
     * 获取本月的开始时间
     *
     * @return 本月
     */
    public static LocalDateTime getMonth() {
        return beginOfMonth(LocalDateTime.now());
    }

    /**
     * 获取本年的开始时间
     *
     * @return 本年
     */
    public static LocalDateTime getYear() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }
}