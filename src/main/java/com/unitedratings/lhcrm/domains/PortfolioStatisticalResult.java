package com.unitedratings.lhcrm.domains;

import java.util.Date;

/**
 * 组合分析统计实体
 */
public class PortfolioStatisticalResult {

    private Long id;
    private Long uploadRecordId;
    /**
     * 标准差
     */
    private double standardDeviation;
    /**
     * 平均违约概率
     */
    private double averageDefaultRate;

    private MonteSummaryResult monteSummaryResult;

    private MonteResult monteResult;

    private PortfolioDefaultDistribution portfolioDefaultDistribution;

    private Date createTime;

    private String resultFilePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUploadRecordId() {
        return uploadRecordId;
    }

    public void setUploadRecordId(Long uploadRecordId) {
        this.uploadRecordId = uploadRecordId;
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

    public MonteResult getMonteResult() {
        return monteResult;
    }

    public void setMonteResult(MonteResult monteResult) {
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
}
