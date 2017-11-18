package com.unitedratings.lhcrm.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangyongxin
 */
@Entity
@Table(indexes = {@Index(columnList = "portfolioId")})
public class DebtorInfo {

    @Id
    @GeneratedValue
    private Long id;
    private Long portfolioId;
    /**
     * 贷款编号
     */
    private Long LoanSerial;
    /**
     * 借款人编号
     */
    private Long borrowerSerial;
    /**
     * 行业代码
     */
    private Long industryCode;
    /**
     * 信用等级
     */
    @Column(length = 10)
    private String creditLevel;
    /**
     * 信用等级代码
     */
    private Integer creditLevelCode;
    private Date maturityDate;
    private Double LoanBalance;
    private Double lendingRate;
    private Double assetSelfRecoveryRate;
    @Column(length = 20)
    private String borrowerArea;
    private Integer borrowerAreaCode;
    private Date loanProvideDate;
    private Boolean isAmortize;
    @Column(length = 10)
    private String ratingOutlook;
    @Column(length = 50)
    private String creditEvent;
    private Boolean isGovernmentFunded;
    private Double defaultMagnification;
    @Column(length = 40)
    private String borrowerIndustry;
    private Double currentMarketValue;
    @Column(length = 40)
    private String borrowerName;
    private Double guaranteeRecoveryRate;
    private Double relevanceForGuaranteeAndLender;
    private Double mortgageRecoveryRate;
    @Column(length = 10)
    private String debtLevel;
    private Integer debtLevelCode;
    /**
     * 最终回收率
     */
    private Double finalRecoveryRate;
    /**
     * 累计违约率
     */
    private Double totalDefaultRate;
    @Column(length = 1000)
    private String defaultRateBySection;
    @Column(length = 1000)
    private String recoveryRateBySection;
    /**
     * 保证金金额
     */
    private Double depositAmount;
    @Column(length = 10)
    private String sovereignCreditLevel;
    private Integer sovereignCreditLevelCode;

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

    public Long getLoanSerial() {
        return LoanSerial;
    }

    public void setLoanSerial(Long loanSerial) {
        LoanSerial = loanSerial;
    }

    public Long getBorrowerSerial() {
        return borrowerSerial;
    }

    public void setBorrowerSerial(Long borrowerSerial) {
        this.borrowerSerial = borrowerSerial;
    }

    public Long getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(Long industryCode) {
        this.industryCode = industryCode;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public Integer getCreditLevelCode() {
        return creditLevelCode;
    }

    public void setCreditLevelCode(Integer creditLevelCode) {
        this.creditLevelCode = creditLevelCode;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Double getLoanBalance() {
        return LoanBalance;
    }

    public void setLoanBalance(Double loanBalance) {
        LoanBalance = loanBalance;
    }

    public Double getLendingRate() {
        return lendingRate;
    }

    public void setLendingRate(Double lendingRate) {
        this.lendingRate = lendingRate;
    }

    public Double getAssetSelfRecoveryRate() {
        return assetSelfRecoveryRate;
    }

    public void setAssetSelfRecoveryRate(Double assetSelfRecoveryRate) {
        this.assetSelfRecoveryRate = assetSelfRecoveryRate;
    }

    public String getBorrowerArea() {
        return borrowerArea;
    }

    public void setBorrowerArea(String borrowerArea) {
        this.borrowerArea = borrowerArea;
    }

    public Integer getBorrowerAreaCode() {
        return borrowerAreaCode;
    }

    public void setBorrowerAreaCode(Integer borrowerAreaCode) {
        this.borrowerAreaCode = borrowerAreaCode;
    }

    public Date getLoanProvideDate() {
        return loanProvideDate;
    }

    public void setLoanProvideDate(Date loanProvideDate) {
        this.loanProvideDate = loanProvideDate;
    }

    public Boolean getAmortize() {
        return isAmortize;
    }

    public void setAmortize(Boolean amortize) {
        isAmortize = amortize;
    }

    public String getRatingOutlook() {
        return ratingOutlook;
    }

    public void setRatingOutlook(String ratingOutlook) {
        this.ratingOutlook = ratingOutlook;
    }

    public String getCreditEvent() {
        return creditEvent;
    }

    public void setCreditEvent(String creditEvent) {
        this.creditEvent = creditEvent;
    }

    public Boolean getGovernmentFunded() {
        return isGovernmentFunded;
    }

    public void setGovernmentFunded(Boolean governmentFunded) {
        isGovernmentFunded = governmentFunded;
    }

    public Double getDefaultMagnification() {
        return defaultMagnification;
    }

    public void setDefaultMagnification(Double defaultMagnification) {
        this.defaultMagnification = defaultMagnification;
    }

    public String getBorrowerIndustry() {
        return borrowerIndustry;
    }

    public void setBorrowerIndustry(String borrowerIndustry) {
        this.borrowerIndustry = borrowerIndustry;
    }

    public Double getCurrentMarketValue() {
        return currentMarketValue;
    }

    public void setCurrentMarketValue(Double currentMarketValue) {
        this.currentMarketValue = currentMarketValue;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Double getGuaranteeRecoveryRate() {
        return guaranteeRecoveryRate;
    }

    public void setGuaranteeRecoveryRate(Double guaranteeRecoveryRate) {
        this.guaranteeRecoveryRate = guaranteeRecoveryRate;
    }

    public Double getRelevanceForGuaranteeAndLender() {
        return relevanceForGuaranteeAndLender;
    }

    public void setRelevanceForGuaranteeAndLender(Double relevanceForGuaranteeAndLender) {
        this.relevanceForGuaranteeAndLender = relevanceForGuaranteeAndLender;
    }

    public Double getMortgageRecoveryRate() {
        return mortgageRecoveryRate;
    }

    public void setMortgageRecoveryRate(Double mortgageRecoveryRate) {
        this.mortgageRecoveryRate = mortgageRecoveryRate;
    }

    public String getDebtLevel() {
        return debtLevel;
    }

    public void setDebtLevel(String debtLevel) {
        this.debtLevel = debtLevel;
    }

    public Integer getDebtLevelCode() {
        return debtLevelCode;
    }

    public void setDebtLevelCode(Integer debtLevelCode) {
        this.debtLevelCode = debtLevelCode;
    }

    public Double getFinalRecoveryRate() {
        return finalRecoveryRate;
    }

    public void setFinalRecoveryRate(Double finalRecoveryRate) {
        this.finalRecoveryRate = finalRecoveryRate;
    }

    public Double getTotalDefaultRate() {
        return totalDefaultRate;
    }

    public void setTotalDefaultRate(Double totalDefaultRate) {
        this.totalDefaultRate = totalDefaultRate;
    }

    public String getDefaultRateBySection() {
        return defaultRateBySection;
    }

    public void setDefaultRateBySection(String defaultRateBySection) {
        this.defaultRateBySection = defaultRateBySection;
    }

    public String getRecoveryRateBySection() {
        return recoveryRateBySection;
    }

    public void setRecoveryRateBySection(String recoveryRateBySection) {
        this.recoveryRateBySection = recoveryRateBySection;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getSovereignCreditLevel() {
        return sovereignCreditLevel;
    }

    public void setSovereignCreditLevel(String sovereignCreditLevel) {
        this.sovereignCreditLevel = sovereignCreditLevel;
    }

    public Integer getSovereignCreditLevelCode() {
        return sovereignCreditLevelCode;
    }

    public void setSovereignCreditLevelCode(Integer sovereignCreditLevelCode) {
        this.sovereignCreditLevelCode = sovereignCreditLevelCode;
    }
}
