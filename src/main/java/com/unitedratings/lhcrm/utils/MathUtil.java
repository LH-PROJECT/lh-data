package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.constants.Constant;
import org.apache.commons.math3.stat.StatUtils;

public class MathUtil {

    private MathUtil(){}

    /**
     * 获取最大到期期限分段数
     * @param arr 到期期限数组
     * @return
     */
    public static Integer getMaxQuarter(double[] arr) {
        return new Double(org.ujmp.core.util.MathUtil.max(arr)).intValue();
    }

    /**
     * 向上取整
     * @param num
     * @return
     */
    public static Integer ceil(double num){
        return new Double(Math.ceil(num)).intValue();
    }

    /**
     * 计算加权平均期限
     * @param principal 本金
     * @param maturity 期限
     * @return
     */
    public static double calculateWeightedAverageMaturity(double[] principal, double[] maturity) {
        double weighted = 0;
        double origin = 0;
        for(int i=0;i<principal.length;i++){
            weighted += principal[i]*maturity[i];
            origin += principal[i];
        }
        return weighted/origin;
    }

    /**
     * 获取位置对应的索引
     * @param arr
     * @param position
     * @return
     */
    public static int getProbabilityIndex(double[] arr, int position) {
        int count=0;
        for(int i=arr.length-1;i>=0;i--){
            count += arr[i];
            if(count>position){
                if(i==arr.length-1){
                    return i;
                }else {
                    return i+1;
                }
            }
        }
        return 0;
    }

    /**
     * 计算标准差
     * @param defaultRate
     * @param num
     * @return
     */
    public static double calculateStandardDeviation(double[] defaultRate, Integer num) {
        double[] actual = new double[defaultRate.length];
        for(int i=0;i<defaultRate.length;i++){
            actual[i] = defaultRate[i]*i/num;
        }
        return Math.sqrt(StatUtils.variance(actual));
    }
}
