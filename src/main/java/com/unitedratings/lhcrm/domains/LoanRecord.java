package com.unitedratings.lhcrm.domains;

import java.util.Date;

public class LoanRecord {

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
    private String creditLevel;
    /**
     * 信用等级代码
     */
    private Integer creditLevelCode;
    private Date maturityDate;
    private Double LoanBalance;
    private Double lendingRate;
    private Double assetSelfRecoveryRate;
    private String lenderArea;
    private Integer lenderAreaCode;
    private Date loanProvideDate;
    private Boolean isAmortize;
    private String guaranteeMode;
    private Integer guaranteeModeCode;
    private String ratingOutlook;
    private String creditEvent;
    private Boolean isGovernmentFunded;
    private Double defaultMagnification;
    private String guaranteeName;
    private String liabilityForm;
    private Integer liabilityFormCode;
    private Double guaranteeRatio;
    private String guaranteeCreditLevel;
    private Integer guaranteeCreditLevelCode;
    private Long guaranteeIndustryCode;
    private String guaranteeBelongArea;
    private String SovereignCreditLevel;
    private Integer SovereignCreditLevelCode;
    private String borrowerIndustry;
    private Long borrowerIndustryCode;
    private Double currentMarketValue;
    private String borrowerName;
    private Double guaranteeRecoveryRate;
    private String guaranteeAgentIndustry;
    private Long guaranteeAgentIndustryCode;
    private Double relevanceForGuaranteeAndLender;
    private Double mortgageRecoveryRate;
    private String debtLevel;
    private Integer debtLevelCode;
    private Double finalRecoveryRate;
    private Double totalDefaultRate;
    private String defaultRateBySection;
    private String recoveryRateBySection;
    private Double depositAmount;

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

    public String getLenderArea() {
        return lenderArea;
    }

    public void setLenderArea(String lenderArea) {
        this.lenderArea = lenderArea;
    }

    public Integer getLenderAreaCode() {
        return lenderAreaCode;
    }

    public void setLenderAreaCode(Integer lenderAreaCode) {
        this.lenderAreaCode = lenderAreaCode;
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

    public String getGuaranteeMode() {
        return guaranteeMode;
    }

    public void setGuaranteeMode(String guaranteeMode) {
        this.guaranteeMode = guaranteeMode;
    }

    public Integer getGuaranteeModeCode() {
        return guaranteeModeCode;
    }

    public void setGuaranteeModeCode(Integer guaranteeModeCode) {
        this.guaranteeModeCode = guaranteeModeCode;
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

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getLiabilityForm() {
        return liabilityForm;
    }

    public void setLiabilityForm(String liabilityForm) {
        this.liabilityForm = liabilityForm;
    }

    public Integer getLiabilityFormCode() {
        return liabilityFormCode;
    }

    public void setLiabilityFormCode(Integer liabilityFormCode) {
        this.liabilityFormCode = liabilityFormCode;
    }

    public Double getGuaranteeRatio() {
        return guaranteeRatio;
    }

    public void setGuaranteeRatio(Double guaranteeRatio) {
        this.guaranteeRatio = guaranteeRatio;
    }

    public String getGuaranteeCreditLevel() {
        return guaranteeCreditLevel;
    }

    public void setGuaranteeCreditLevel(String guaranteeCreditLevel) {
        this.guaranteeCreditLevel = guaranteeCreditLevel;
    }

    public Integer getGuaranteeCreditLevelCode() {
        return guaranteeCreditLevelCode;
    }

    public void setGuaranteeCreditLevelCode(Integer guaranteeCreditLevelCode) {
        this.guaranteeCreditLevelCode = guaranteeCreditLevelCode;
    }

    public Long getGuaranteeIndustryCode() {
        return guaranteeIndustryCode;
    }

    public void setGuaranteeIndustryCode(Long guaranteeIndustryCode) {
        this.guaranteeIndustryCode = guaranteeIndustryCode;
    }

    public String getGuaranteeBelongArea() {
        return guaranteeBelongArea;
    }

    public void setGuaranteeBelongArea(String guaranteeBelongArea) {
        this.guaranteeBelongArea = guaranteeBelongArea;
    }

    public String getSovereignCreditLevel() {
        return SovereignCreditLevel;
    }

    public void setSovereignCreditLevel(String sovereignCreditLevel) {
        SovereignCreditLevel = sovereignCreditLevel;
    }

    public Integer getSovereignCreditLevelCode() {
        return SovereignCreditLevelCode;
    }

    public void setSovereignCreditLevelCode(Integer sovereignCreditLevelCode) {
        SovereignCreditLevelCode = sovereignCreditLevelCode;
    }

    public String getBorrowerIndustry() {
        return borrowerIndustry;
    }

    public void setBorrowerIndustry(String borrowerIndustry) {
        this.borrowerIndustry = borrowerIndustry;
    }

    public Long getBorrowerIndustryCode() {
        return borrowerIndustryCode;
    }

    public void setBorrowerIndustryCode(Long borrowerIndustryCode) {
        this.borrowerIndustryCode = borrowerIndustryCode;
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

    public String getGuaranteeAgentIndustry() {
        return guaranteeAgentIndustry;
    }

    public void setGuaranteeAgentIndustry(String guaranteeAgentIndustry) {
        this.guaranteeAgentIndustry = guaranteeAgentIndustry;
    }

    public Long getGuaranteeAgentIndustryCode() {
        return guaranteeAgentIndustryCode;
    }

    public void setGuaranteeAgentIndustryCode(Long guaranteeAgentIndustryCode) {
        this.guaranteeAgentIndustryCode = guaranteeAgentIndustryCode;
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
}
