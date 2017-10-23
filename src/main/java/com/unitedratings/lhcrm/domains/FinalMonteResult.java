package com.unitedratings.lhcrm.domains;

/**
 * @author wangyongxin
 */
public class FinalMonteResult extends MonteResult{
    /**
     * 存放按季度的违约比率
     */
    private double[] defaultRateByPeriod;
    /**
     * 存放资产池平均回收率
     */
    private Double averageRecoveryRate;

    public double[] getDefaultRateByPeriod() {
        return defaultRateByPeriod;
    }

    public void setDefaultRateByPeriod(double[] defaultRateByPeriod) {
        this.defaultRateByPeriod = defaultRateByPeriod;
    }

    public Double getAverageRecoveryRate() {
        return averageRecoveryRate;
    }

    public void setAverageRecoveryRate(Double averageRecoveryRate) {
        this.averageRecoveryRate = averageRecoveryRate;
    }
}
