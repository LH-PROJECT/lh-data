package com.unitedratings.lhcrm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author wangyongxin
 */
public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 按统计维度计算两个日期之间相差的区间数，若待计算日期小于基准日期返回0。
     * @param summaryType 汇总维度
     * @param beginCalculateDate 基准日期
     * @param endDate 待计算日期
     * @return
     */
    public static double calculatePeriods(Integer summaryType,Date beginCalculateDate, Date endDate) {
        if(beginCalculateDate==null||endDate==null){
            return 0;
        }

        final LocalDate begin = LocalDateTime.ofInstant(beginCalculateDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        final LocalDate end = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        final long days = end.toEpochDay() - begin.toEpochDay();
        final Period p = Period.between(begin, end);
        if(days<=0){
            return 0;
        }
        double period = 0;
        switch (summaryType){
            case 1:
                //按年
                period = p.getYears() + (double) end.getDayOfYear()/end.lengthOfYear();
                break;
            case 2:
                //按季度
                period = calculateQuarterInternal(begin, end, p);
                break;
            case 3:
                //按月
                period = p.toTotalMonths() + (double) p.getDays()/end.lengthOfMonth();
                break;
            case 4:
                //按周
                period = (double) days/7;
                break;
            case 5:
                //按天
                period = days;
                break;
            default:
                break;
        }
        return period;
    }

    /**
     * 计算季度数
     * @param begin
     * @param end
     * @param p
     * @return
     */
    private static double calculateQuarterInternal(LocalDate begin, LocalDate end, final Period p) {
        double period;
        int totalMonths = (int) p.toTotalMonths();
        int quarter = totalMonths/3;
        int remainder = totalMonths%3;
        if(remainder==0){
            period = quarter + (double) p.getDays() / end.lengthOfMonth();
        } else {
            LocalDate lowDate = begin.plusMonths( quarter*3);
            LocalDate upDate = begin.plusMonths((quarter+1)*3);
            period = quarter + (double) (begin.toEpochDay()-lowDate.toEpochDay())/(upDate.toEpochDay()-lowDate.toEpochDay());
        }
        return period;
    }

    /**
     * 解析日期
     * @param dateStr 带解析字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parseDate(String dateStr,String pattern){
        if(!StringUtils.isEmpty(dateStr)){
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                LOGGER.error("日期解析失败",e);
            }
        }
        return null;
    }

    /**
     * 获取系统时间戳(日期格式的)
     * @return
     */
    public static String getTimestamp() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return time.format(formatter);
    }

    /**
     * 格式化时间
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 计算两个日期之间的季度数
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 间隔季度数
     */
    public static double calculateQuarterNum(LocalDate beginDate, LocalDate endDate) {
        if(beginDate.isAfter(endDate)){
            return 0;
        }
        return calculateQuarterInternal(beginDate,endDate,Period.between(beginDate, endDate));
    }
}
