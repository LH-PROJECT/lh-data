package com.unitedratings.lhcrm.domains;

public class FinalMonteResult extends MonteResult{
    /**
     * 存放按季度的违约比率
     */
    private double[] defaultRateByPeriod;

    public double[] getDefaultRateByPeriod() {
        return defaultRateByPeriod;
    }

    public void setDefaultRateByPeriod(double[] defaultRateByPeriod) {
        this.defaultRateByPeriod = defaultRateByPeriod;
    }
}
