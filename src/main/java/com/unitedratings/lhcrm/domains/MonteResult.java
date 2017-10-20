package com.unitedratings.lhcrm.domains;

import org.ujmp.core.Matrix;

/**
 * 蒙特卡洛模拟结果实体
 * @author wangyongxin
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
     * 存放按年/季违约记录数据
     */
    private transient Matrix defaultRecordMatrix;
    /**
     * 总违约金额
     */
    private double sumDefault;
    /**
     * 总违约率
     */
    private double sumDefaultRate;

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

    public Matrix getDefaultRecordMatrix() {
        return defaultRecordMatrix;
    }

    public void setDefaultRecordMatrix(Matrix defaultRecordMatrix) {
        this.defaultRecordMatrix = defaultRecordMatrix;
    }

    public double getSumDefault() {
        return sumDefault;
    }

    public void setSumDefault(double sumDefault) {
        this.sumDefault = sumDefault;
    }

    public double getSumDefaultRate() {
        return sumDefaultRate;
    }

    public void setSumDefaultRate(double sumDefaultRate) {
        this.sumDefaultRate = sumDefaultRate;
    }
}
