package com.unitedratings.lhcrm.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PortfolioAnalysisResult {

    @Id
    @GeneratedValue
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

    @Lob
    private String monteSummaryResult;

    @Lob
    private String monteResult;

    @Lob
    private String portfolioDefaultDistribution;

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

    public String getMonteSummaryResult() {
        return monteSummaryResult;
    }

    public void setMonteSummaryResult(String monteSummaryResult) {
        this.monteSummaryResult = monteSummaryResult;
    }

    public String getMonteResult() {
        return monteResult;
    }

    public void setMonteResult(String monteResult) {
        this.monteResult = monteResult;
    }

    public String getPortfolioDefaultDistribution() {
        return portfolioDefaultDistribution;
    }

    public void setPortfolioDefaultDistribution(String portfolioDefaultDistribution) {
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

    public double getAverageDefaultRate() {
        return averageDefaultRate;
    }

    public void setAverageDefaultRate(double averageDefaultRate) {
        this.averageDefaultRate = averageDefaultRate;
    }
}
