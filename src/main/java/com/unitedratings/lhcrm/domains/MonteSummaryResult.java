package com.unitedratings.lhcrm.domains;

/**
 * @author wangyongxin
 */
public class MonteSummaryResult {

    private double[] targetDefaultProbability;
    private double[] targetDefaultRate;
    private double[] targetRecoveryRate;
    private double[] targetLossRate;

    public double[] getTargetDefaultProbability() {
        return targetDefaultProbability;
    }

    public void setTargetDefaultProbability(double[] targetDefaultProbability) {
        this.targetDefaultProbability = targetDefaultProbability;
    }

    public double[] getTargetDefaultRate() {
        return targetDefaultRate;
    }

    public void setTargetDefaultRate(double[] targetDefaultRate) {
        this.targetDefaultRate = targetDefaultRate;
    }

    public double[] getTargetRecoveryRate() {
        return targetRecoveryRate;
    }

    public void setTargetRecoveryRate(double[] targetRecoveryRate) {
        this.targetRecoveryRate = targetRecoveryRate;
    }

    public double[] getTargetLossRate() {
        return targetLossRate;
    }

    public void setTargetLossRate(double[] targetLossRate) {
        this.targetLossRate = targetLossRate;
    }
}
