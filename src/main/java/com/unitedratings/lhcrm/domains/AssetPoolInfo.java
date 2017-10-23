package com.unitedratings.lhcrm.domains;

import org.ujmp.core.Matrix;

import java.util.Date;

/**
 * @author wangyongxin
 */
public class AssetPoolInfo {

    /**
     * 资产池名称
     */
    private String portfolioName;

    /**
     * 贷款编号
     */
    private long[] loanSerial;
    /**
     * 贷款人编号
     */
    private long[] ids;
    /**
     * 资产类型(行业编码)
     */
    private long[] assetType;
    /**
     * 资产相关系数矩阵
     */
    private Matrix correlation;
    /**
     * 本金
     */
    private double[] principal;
    /**
     * 到期年限
     */
    private double[] maturity;
    private double[] yearMaturity;
    private Matrix rrTime;
    /**
     * 贷款笔数
     */
    private Integer loanNum;
    /**
     * 保证金
     */
    private double[] secureAmount;
    /**
     * 准备金（前台传递）
     */
    private double reservesMoney;

    /**
     * 分期摊还
     */
    private Matrix amortisation;
    /**
     * 初始起算日（前台传递）
     */
    private Date beginCalculateDate;

    /**
     * 违约率计算汇总维度，默认按季度汇总
     */
    private Integer summaryType = 2;

    /**
     * 最终回收率（需要计算，暂时使用模板内的）
     */
    private double[] finalRecoveryRate;

    /**
     * 理想违约率矩阵
     */
    private Matrix perfectDefaultRate;

    /**
     * 条件违约概率（逐年，临时使用）
     */
    private Matrix conditionMatrix;

    private double weightedAverageMaturity;

    /**
     * 关联的理想违约率表id
     */
    private Integer idealDefaultId;

    /**
     * 资产违约率
     */
    private double[] assetDR;

    /**
     * 担保人违约率
     */
    private double[] scDR;

    /**
     * 债项违约率
     */
    private double[] loanPD;

    public long[] getLoanSerial() {
        return loanSerial;
    }

    public void setLoanSerial(long[] loanSerial) {
        this.loanSerial = loanSerial;
    }

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    public long[] getAssetType() {
        return assetType;
    }

    public void setAssetType(long[] assetType) {
        this.assetType = assetType;
    }

    public Matrix getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Matrix correlation) {
        this.correlation = correlation;
    }

    public double[] getPrincipal() {
        return principal;
    }

    public void setPrincipal(double[] principal) {
        this.principal = principal;
    }

    public double[] getMaturity() {
        return maturity;
    }

    public void setMaturity(double[] maturity) {
        this.maturity = maturity;
    }

    public Matrix getRrTime() {
        return rrTime;
    }

    public void setRrTime(Matrix rrTime) {
        this.rrTime = rrTime;
    }

    public Integer getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(Integer loanNum) {
        this.loanNum = loanNum;
    }

    public double[] getSecureAmount() {
        return secureAmount;
    }

    public void setSecureAmount(double[] secureAmount) {
        this.secureAmount = secureAmount;
    }

    public Matrix getAmortisation() {
        return amortisation;
    }

    public void setAmortisation(Matrix amortisation) {
        this.amortisation = amortisation;
    }

    public double getReservesMoney() {
        return reservesMoney;
    }

    public void setReservesMoney(double reservesMoney) {
        this.reservesMoney = reservesMoney;
    }

    public Date getBeginCalculateDate() {
        return beginCalculateDate;
    }

    public void setBeginCalculateDate(Date beginCalculateDate) {
        this.beginCalculateDate = beginCalculateDate;
    }

    public Integer getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(Integer summaryType) {
        this.summaryType = summaryType;
    }

    public double[] getFinalRecoveryRate() {
        return finalRecoveryRate;
    }

    public void setFinalRecoveryRate(double[] finalRecoveryRate) {
        this.finalRecoveryRate = finalRecoveryRate;
    }

    public Matrix getPerfectDefaultRate() {
        return perfectDefaultRate;
    }

    public void setPerfectDefaultRate(Matrix perfectDefaultRate) {
        this.perfectDefaultRate = perfectDefaultRate;
    }

    public Matrix getConditionMatrix() {
        return conditionMatrix;
    }

    public void setConditionMatrix(Matrix conditionMatrix) {
        this.conditionMatrix = conditionMatrix;
    }

    public double getWeightedAverageMaturity() {
        return weightedAverageMaturity;
    }

    public void setWeightedAverageMaturity(double weightedAverageMaturity) {
        this.weightedAverageMaturity = weightedAverageMaturity;
    }

    public Integer getIdealDefaultId() {
        return idealDefaultId;
    }

    public void setIdealDefaultId(Integer idealDefaultId) {
        this.idealDefaultId = idealDefaultId;
    }

    public double[] getYearMaturity() {
        return yearMaturity;
    }

    public void setYearMaturity(double[] yearMaturity) {
        this.yearMaturity = yearMaturity;
    }

    public double[] getAssetDR() {
        return assetDR;
    }

    public void setAssetDR(double[] assetDR) {
        this.assetDR = assetDR;
    }

    public double[] getScDR() {
        return scDR;
    }

    public void setScDR(double[] scDR) {
        this.scDR = scDR;
    }

    public double[] getLoanPD() {
        return loanPD;
    }

    public void setLoanPD(double[] loanPD) {
        this.loanPD = loanPD;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }
}
