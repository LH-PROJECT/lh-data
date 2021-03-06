package com.unitedratings.lhcrm.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangyongxin
 */
@Entity
@Table(indexes = {@Index(columnList = "portfolioId")})
public class PortfolioAnalysisResult {

    @Id
    @GeneratedValue
    private Long id;
    private Long portfolioId;
    /**
     * 标准差
     */
    private Double standardDeviation;
    /**
     * 平均违约概率
     */
    private Double averageRecoveryRate;

    /**
     * 平均回收概率
     */
    private Double averageDefaultRate;

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

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
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

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Double getAverageRecoveryRate() {
        return averageRecoveryRate;
    }

    public void setAverageRecoveryRate(Double averageRecoveryRate) {
        this.averageRecoveryRate = averageRecoveryRate;
    }

    public Double getAverageDefaultRate() {
        return averageDefaultRate;
    }

    public void setAverageDefaultRate(Double averageDefaultRate) {
        this.averageDefaultRate = averageDefaultRate;
    }
}
