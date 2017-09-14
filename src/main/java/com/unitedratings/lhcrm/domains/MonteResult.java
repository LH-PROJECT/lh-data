package com.unitedratings.lhcrm.domains;

/**
 * 蒙特卡洛模拟结果实体
 */
public class MonteResult {

    /**
     * 存放违约率
     */
    private double[] defaultRate;
    /**
     * 存放回收率
     */
    private double[] recoveryRate;
    /**
     * 存放损失率
     */
    private double[] lossRate;
    /**
     * 存放按季度的违约比率
     */
    private double[] defaultRateByPeriod;


    public double[] getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(double[] defaultRate) {
        this.defaultRate = defaultRate;
    }

    public double[] getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double[] recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public double[] getLossRate() {
        return lossRate;
    }

    public void setLossRate(double[] lossRate) {
        this.lossRate = lossRate;
    }

    public double[] getDefaultRateByPeriod() {
        return defaultRateByPeriod;
    }

    public void setDefaultRateByPeriod(double[] defaultRateByPeriod) {
        this.defaultRateByPeriod = defaultRateByPeriod;
    }
}
