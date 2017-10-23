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
        //final Period p = Period.between(begin, end);
        if(days<0){
            return 0;
        }
        double period = 0;
        switch (summaryType){
            case 1://按年
                period = (double) days/365;
                break;
            case 2://按季度
                period = (double) days/90;
                /*if(p.getYears()>0){
                    period = p.getYears()*4;
                }
                if(p.getMonths()%3==0){
                    period += p.getMonths()/3;
                    if(p.getDays()>0){
                        period += 1;
                    }
                }else {
                    period += p.getMonths()/3 + 1;
                }*/
                break;
            case 3://按月
                period = (double) days/30;
                break;
            case 4://按周
                period = (double) days/7;
                break;
            case 5://按天
                period = days;
                break;
            default:
                break;
        }
        return period;
    }

    /**
     * 获取相差季度数
     * @param beginCalculateDate
     * @param endDate
     * @return
     */
    public static double calculateQuarter(Date beginCalculateDate, Date endDate) {
        if(beginCalculateDate==null||endDate==null){
            return 0;
        }

        final LocalDate begin = LocalDateTime.ofInstant(beginCalculateDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        final LocalDate end = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        final Period p = Period.between(begin, end);

        double period = 0;

        if(p.getYears()>0){
            period = p.getYears()*4;
        }

        if(p.getMonths()%3==0){
            period += p.getMonths()/3;
            if(p.getDays()>0){
                period += 1;
            }
        }else {
            period += p.getMonths()/3 + 1;
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
}
