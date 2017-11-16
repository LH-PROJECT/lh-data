package com.unitedratings.lhcrm.web.model;

import java.util.Date;

/**
 * @author wangyongxin
 * @createAt 2017-11-16 下午3:46
 **/
public class LoanRecordVo {

    private Long debtInfoId;
    private Long guarantorInfoId;
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
    private String creditLevel;
    /**
     * 信用等级代码
     */
    private Integer creditLevelCode;
    private Date maturityDate;
    private Double LoanBalance;
    private Double lendingRate;
    private Double assetSelfRecoveryRate;
    private String borrowerArea;
    private Integer borrowerAreaCode;
    private Date loanProvideDate;
    private Boolean isAmortize;
    private String ratingOutlook;
    private String creditEvent;
    private Boolean isGovernmentFunded;
    private Double defaultMagnification;
    private String borrowerIndustry;
    private Double currentMarketValue;
    private String borrowerName;
    private Double guaranteeRecoveryRate;
    private Double relevanceForGuaranteeAndLender;
    private Double mortgageRecoveryRate;
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
    private String defaultRateBySection;
    private String recoveryRateBySection;
    /**
     * 保证金金额
     */
    private Double depositAmount;
    private String sovereignCreditLevel;
    private Integer sovereignCreditLevelCode;
    private String guaranteeMode;
    private String guaranteeModeCode;
    private Integer guaranteeModeId;
    private String guaranteeName;
    private String liabilityForm;
    private Integer liabilityFormCode;
    private Double guaranteeRatio;
    private String guaranteeCreditLevel;
    private Integer guaranteeCreditLevelCode;
    private Long guaranteeIndustryCode;
    private String guaranteeAgentIndustry;
    private String guaranteeBelongArea;
    private Integer guaranteeNum;

    public Long getDebtInfoId() {
        return debtInfoId;
    }

    public void setDebtInfoId(Long debtInfoId) {
        this.debtInfoId = debtInfoId;
    }

    public Long getGuarantorInfoId() {
        return guarantorInfoId;
    }

    public void setGuarantorInfoId(Long guarantorInfoId) {
        this.guarantorInfoId = guarantorInfoId;
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

    public String getGuaranteeMode() {
        return guaranteeMode;
    }

    public void setGuaranteeMode(String guaranteeMode) {
        this.guaranteeMode = guaranteeMode;
    }

    public String getGuaranteeModeCode() {
        return guaranteeModeCode;
    }

    public void setGuaranteeModeCode(String guaranteeModeCode) {
        this.guaranteeModeCode = guaranteeModeCode;
    }

    public Integer getGuaranteeModeId() {
        return guaranteeModeId;
    }

    public void setGuaranteeModeId(Integer guaranteeModeId) {
        this.guaranteeModeId = guaranteeModeId;
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

    public String getGuaranteeAgentIndustry() {
        return guaranteeAgentIndustry;
    }

    public void setGuaranteeAgentIndustry(String guaranteeAgentIndustry) {
        this.guaranteeAgentIndustry = guaranteeAgentIndustry;
    }

    public String getGuaranteeBelongArea() {
        return guaranteeBelongArea;
    }

    public void setGuaranteeBelongArea(String guaranteeBelongArea) {
        this.guaranteeBelongArea = guaranteeBelongArea;
    }

    public Integer getGuaranteeNum() {
        return guaranteeNum;
    }

    public void setGuaranteeNum(Integer guaranteeNum) {
        this.guaranteeNum = guaranteeNum;
    }
}
