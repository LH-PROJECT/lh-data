package com.unitedratings.lhcrm.domains;

/**
 * 组合违约分布
 * @author wangyongxin
 */
public class PortfolioDefaultDistribution {

    private double[] defaultProbability;
    private double[] lossProbability;

    public double[] getDefaultProbability() {
        return defaultProbability;
    }

    public void setDefaultProbability(double[] defaultProbability) {
        this.defaultProbability = defaultProbability;
    }

    public double[] getLossProbability() {
        return lossProbability;
    }

    public void setLossProbability(double[] lossProbability) {
        this.lossProbability = lossProbability;
    }
}
