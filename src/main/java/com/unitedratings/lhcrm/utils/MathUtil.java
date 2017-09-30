package com.unitedratings.lhcrm.utils;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

public class MathUtil {

    private MathUtil(){}

    /**
     * 获取最大到期期限分段数
     * @param arr 到期期限数组
     * @return
     */
    public static Integer getMaxQuarter(double[] arr) {
        return (int) Math.ceil(org.ujmp.core.util.MathUtil.max(arr));
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

    /**
     * 二元正态分布累计分布函数
     * @param z1
     * @param z2
     * @param correlation
     * @return
     */
    public static double binNormalDistributionCumulativeFunction(double z1,double z2,double correlation){
        double[] x = new double[12];
        double[] w = new double[12];
        final double lowerLimit = -10;
        final double sqr2pi = Math.sqrt(2*Math.PI);
        x[0]= 0.0640568928626056;
        w[0]= 0.127938195346752;
        x[1]= 0.191118867473616;
        w[1]= 0.125837456346828;
        x[2]= 0.315042679696163;
        w[2]= 0.121670472927803;
        x[3]= 0.433793507626045;
        w[3]= 0.115505668053726;
        x[4]= 0.54542147138884;
        w[4]= 0.107444270115966;
        x[5]= 0.648093651936976;
        w[5]= 0.0976186521041137;
        x[6]= 0.740124191578554;
        w[6]= 0.0861901615319532;
        x[7]= 0.820001985973903;
        w[7]= 0.0733464814110803;
        x[8]= 0.886415527004401;
        w[8]= 0.0592985849154367;
        x[9] = 0.938274552002733;
        w[9] = 0.0442774388174197;
        x[10] = 0.974728555971309;
        w[10] = 0.0285313886289337;
        x[11] = 0.995187219997021;
        w[11] = 0.0123412297999865;
        double xm = 0.5 * (z1 + lowerLimit);
        double xl = 0.5 * (z1 - lowerLimit);
        double integral = 0;
        NormalDistribution normal = new NormalDistribution();
        for(int i=0;i<12;i++){
            double temp = xl * x[i];
            double xx = xm + temp;
            integral += w[i] * xl * (1/sqr2pi) * FastMath.exp(-(xx*xx)/2)*normal.cumulativeProbability((z2-correlation*xx)/Math.sqrt(1-correlation*correlation));
            xx = xm - temp;
            integral += w[i] * xl * (1/sqr2pi) * FastMath.exp(-(xx*xx)/2)*normal.cumulativeProbability((z2-correlation*xx)/Math.sqrt(1-correlation*correlation));
        }
        return integral;
    }
}
