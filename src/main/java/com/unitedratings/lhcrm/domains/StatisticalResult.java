package com.unitedratings.lhcrm.domains;

import java.util.Date;

/**
 * 资产池统计量
 * @author wangyongxin
 * @createAt 2017-10-19 下午5:35
 **/
public class StatisticalResult {

    private String transactionName;
    private Date beginCalculateDate;
    private Long simulationTimes;
    private String assetServiceInstitution;
    private String sponsorOrganization;
    private String trustInstitution;
    private Integer loanNum;
    private Integer debtorNum;
    private Double outstandingPrincipal;
    private Double weightedYearMaturity;
    private Double longestMaturity;
    private Double shortestMaturity;
    private Double weightedAverageRecoverRate;
    private String weightedDebtorCreditLevel;
    private Double weightedDebtorDefaultRate;
    private String weightedLoanCreditLevel;
    private Double weightedLoanDefaultRate;
    private Double aging;
    private Double weightedAverageInterestRate;
    private Double weightedDebtorSelfRecoverRate;
    private Double weightedGuaranteePromotedRecoverRate;
    private Double weightedCollateralAverageRecoverRate;

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Date getBeginCalculateDate() {
        return beginCalculateDate;
    }

    public void setBeginCalculateDate(Date beginCalculateDate) {
        this.beginCalculateDate = beginCalculateDate;
    }

    public Long getSimulationTimes() {
        return simulationTimes;
    }

    public void setSimulationTimes(Long simulationTimes) {
        this.simulationTimes = simulationTimes;
    }

    public String getAssetServiceInstitution() {
        return assetServiceInstitution;
    }

    public void setAssetServiceInstitution(String assetServiceInstitution) {
        this.assetServiceInstitution = assetServiceInstitution;
    }

    public String getSponsorOrganization() {
        return sponsorOrganization;
    }

    public void setSponsorOrganization(String sponsorOrganization) {
        this.sponsorOrganization = sponsorOrganization;
    }

    public String getTrustInstitution() {
        return trustInstitution;
    }

    public void setTrustInstitution(String trustInstitution) {
        this.trustInstitution = trustInstitution;
    }

    public Integer getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(Integer loanNum) {
        this.loanNum = loanNum;
    }

    public Integer getDebtorNum() {
        return debtorNum;
    }

    public void setDebtorNum(Integer debtorNum) {
        this.debtorNum = debtorNum;
    }

    public Double getOutstandingPrincipal() {
        return outstandingPrincipal;
    }

    public void setOutstandingPrincipal(Double outstandingPrincipal) {
        this.outstandingPrincipal = outstandingPrincipal;
    }

    public Double getWeightedYearMaturity() {
        return weightedYearMaturity;
    }

    public void setWeightedYearMaturity(Double weightedYearMaturity) {
        this.weightedYearMaturity = weightedYearMaturity;
    }

    public Double getLongestMaturity() {
        return longestMaturity;
    }

    public void setLongestMaturity(Double longestMaturity) {
        this.longestMaturity = longestMaturity;
    }

    public Double getShortestMaturity() {
        return shortestMaturity;
    }

    public void setShortestMaturity(Double shortestMaturity) {
        this.shortestMaturity = shortestMaturity;
    }

    public Double getWeightedAverageRecoverRate() {
        return weightedAverageRecoverRate;
    }

    public void setWeightedAverageRecoverRate(Double weightedAverageRecoverRate) {
        this.weightedAverageRecoverRate = weightedAverageRecoverRate;
    }

    public String getWeightedDebtorCreditLevel() {
        return weightedDebtorCreditLevel;
    }

    public void setWeightedDebtorCreditLevel(String weightedDebtorCreditLevel) {
        this.weightedDebtorCreditLevel = weightedDebtorCreditLevel;
    }

    public Double getWeightedDebtorDefaultRate() {
        return weightedDebtorDefaultRate;
    }

    public void setWeightedDebtorDefaultRate(Double weightedDebtorDefaultRate) {
        this.weightedDebtorDefaultRate = weightedDebtorDefaultRate;
    }

    public String getWeightedLoanCreditLevel() {
        return weightedLoanCreditLevel;
    }

    public void setWeightedLoanCreditLevel(String weightedLoanCreditLevel) {
        this.weightedLoanCreditLevel = weightedLoanCreditLevel;
    }

    public Double getWeightedLoanDefaultRate() {
        return weightedLoanDefaultRate;
    }

    public void setWeightedLoanDefaultRate(Double weightedLoanDefaultRate) {
        this.weightedLoanDefaultRate = weightedLoanDefaultRate;
    }

    public Double getAging() {
        return aging;
    }

    public void setAging(Double aging) {
        this.aging = aging;
    }

    public Double getWeightedAverageInterestRate() {
        return weightedAverageInterestRate;
    }

    public void setWeightedAverageInterestRate(Double weightedAverageInterestRate) {
        this.weightedAverageInterestRate = weightedAverageInterestRate;
    }

    public Double getWeightedDebtorSelfRecoverRate() {
        return weightedDebtorSelfRecoverRate;
    }

    public void setWeightedDebtorSelfRecoverRate(Double weightedDebtorSelfRecoverRate) {
        this.weightedDebtorSelfRecoverRate = weightedDebtorSelfRecoverRate;
    }

    public Double getWeightedGuaranteePromotedRecoverRate() {
        return weightedGuaranteePromotedRecoverRate;
    }

    public void setWeightedGuaranteePromotedRecoverRate(Double weightedGuaranteePromotedRecoverRate) {
        this.weightedGuaranteePromotedRecoverRate = weightedGuaranteePromotedRecoverRate;
    }

    public Double getWeightedCollateralAverageRecoverRate() {
        return weightedCollateralAverageRecoverRate;
    }

    public void setWeightedCollateralAverageRecoverRate(Double weightedCollateralAverageRecoverRate) {
        this.weightedCollateralAverageRecoverRate = weightedCollateralAverageRecoverRate;
    }
}
