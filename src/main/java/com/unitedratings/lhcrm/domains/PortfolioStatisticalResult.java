package com.unitedratings.lhcrm.domains;

import java.util.Date;

/**
 * 组合分析统计实体
 * @author wangyongxin
 */
public class PortfolioStatisticalResult {

    private Long id;
    private Long portfolioId;
    /**
     * 标准差
     */
    private double standardDeviation;
    /**
     * 平均违约概率
     */
    private double averageDefaultRate;
    /**
     * 平均回收概率
     */
    private double averageRecoveryRate;

    private MonteSummaryResult monteSummaryResult;

    private FinalMonteResult monteResult;

    private PortfolioDefaultDistribution portfolioDefaultDistribution;

    private Date createTime;

    private String resultFilePath;

    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getAverageDefaultRate() {
        return averageDefaultRate;
    }

    public void setAverageDefaultRate(double averageDefaultRate) {
        this.averageDefaultRate = averageDefaultRate;
    }

    public MonteSummaryResult getMonteSummaryResult() {
        return monteSummaryResult;
    }

    public void setMonteSummaryResult(MonteSummaryResult monteSummaryResult) {
        this.monteSummaryResult = monteSummaryResult;
    }

    public FinalMonteResult getMonteResult() {
        return monteResult;
    }

    public void setMonteResult(FinalMonteResult monteResult) {
        this.monteResult = monteResult;
    }

    public PortfolioDefaultDistribution getPortfolioDefaultDistribution() {
        return portfolioDefaultDistribution;
    }

    public void setPortfolioDefaultDistribution(PortfolioDefaultDistribution portfolioDefaultDistribution) {
        this.portfolioDefaultDistribution = portfolioDefaultDistribution;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getAverageRecoveryRate() {
        return averageRecoveryRate;
    }

    public void setAverageRecoveryRate(double averageRecoveryRate) {
        this.averageRecoveryRate = averageRecoveryRate;
    }
}
