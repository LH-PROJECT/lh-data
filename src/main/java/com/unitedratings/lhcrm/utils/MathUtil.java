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
    public static Integer round(double num){
        return new Double(Math.round(num)).intValue();
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
                if(i==arr.length-1||i==0){
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

    /**
     * 一元正态分布累计函数
     * @param x
     * @return
     */
    public static double normalDistributionFunction(double x){
        double a0 = 0.5;
        double a1 = 0.398942280444;
        double a2 = 0.399903438505;
        double a3 = 5.75885480458;
        double a4 = 29.8213557808;
        double a5 = 2.62433121679;
        double a6 = 48.6959930692;
        double a7 = 5.92885724438;

        double b0 = 0.398942280385;
        double b1 = 3.8052 * FastMath.pow(10,-8);
        double b2 = 1.00000615302;
        double b3 = 3.98064794 * FastMath.pow(10,-4);
        double b4 = 1.98615381364;
        double b5 = 0.151679116635;
        double b6 = 5.29330324926;
        double b7 = 4.8385912808;
        double b8 = 15.1508972451;
        double b9 = 0.742380924027;
        double b10 = 30.789933034;
        double b11 = 3.99019417011;

        double zabs = FastMath.abs(x);
        double q = 0;
        if(zabs<=12.7){
            double y = a0*x*x;
            double pdf = FastMath.exp(-y)*b0;
            if(zabs<=1.28){
                double temp = y + a3 - a4 / (y + a5 + a6 / (y + a7));
                q = a0 - zabs * (a1 - a2 * y / temp);
            }else {
                double temp = (zabs - b5 + b6 / (zabs + b7 - b8 / (zabs + b9 + b10 / (zabs + b11))));
                q = pdf / (zabs - b1 + (b2 / (zabs + b3 + b4 / temp)));
            }
        }

        if(x<0){
            return q;
        }else {
            return 1-q;
        }
    }


    /**
     * 一元正态累计分布逆函数
     * @param p
     * @return
     */
    public static double inverseCumulativeProbability(double p){
        double a1 = -39.6968302866538;
        double a2 = 220.946098424521;
        double a3 = -275.928510446969;
        double a4 = 138.357751867269;
        double a5 = -30.6647980661472;
        double a6 = 2.50662827745924;

        double b1 = -54.4760987982241;
        double b2 = 161.585836858041;
        double b3 = -155.698979859887;
        double b4 = 66.8013118877197;
        double b5 = -13.2806815528857;

        double c1 = -0.00778489400243029;
        double c2 = -0.322396458041136;
        double c3 = -2.40075827716184;
        double c4 = -2.54973253934373;
        double c5 = 4.37466414146497;
        double c6 = 2.93816398269878;

        double d1 = 0.00778469570904146;
        double d2 = 0.32246712907004;
        double d3 = 2.445134137143;
        double d4 = 3.75440866190742;

        double p_low = 0.02425;
        double p_high = 1 - p_low;

        double result = 0;
        if(p<p_low){
            double q = FastMath.sqrt(-2 * FastMath.log(p));
            result = (((((c1 * q + c2) * q + c3) * q + c4) * q + c5) * q + c6) / ((((d1 * q + d2) * q + d3) * q + d4) * q + 1);
        }else if(p<=p_high){
            double q = p - 0.5;
            double R = q * q;
            result = (((((a1 * R + a2) * R + a3) * R + a4) * R + a5) * R + a6) * q / (((((b1 * R + b2) * R + b3) * R + b4) * R + b5) * R + 1);
        }else if(p<1){
            double q = FastMath.sqrt(-2 * FastMath.log(1 - p));
            result = -(((((c1 * q + c2) * q + c3) * q + c4) * q + c5) * q + c6) / ((((d1 * q + d2) * q + d3) * q + d4) * q + 1);
        }

        return result;
    }

}
