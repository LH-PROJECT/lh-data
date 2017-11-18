package com.unitedratings.lhcrm.domains;

import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-10-31 上午10:19
 **/
public class LargeAmountResult {
    private String targetCreditLevel;
    private Integer targetCreditLevelCode;
    private Double minimumSupport;
    private List<Double> grossAmountList;
    private List<Double> grossAmountListAfter5PercentRecovery;
    private List<Integer> levelDifference;
    private List<LargeAmountLoan> largeAmountLoanList;

    public String getTargetCreditLevel() {
        return targetCreditLevel;
    }

    public void setTargetCreditLevel(String targetCreditLevel) {
        this.targetCreditLevel = targetCreditLevel;
    }

    public Integer getTargetCreditLevelCode() {
        return targetCreditLevelCode;
    }

    public void setTargetCreditLevelCode(Integer targetCreditLevelCode) {
        this.targetCreditLevelCode = targetCreditLevelCode;
    }

    public Double getMinimumSupport() {
        return minimumSupport;
    }

    public void setMinimumSupport(Double minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public List<Double> getGrossAmountList() {
        return grossAmountList;
    }

    public void setGrossAmountList(List<Double> grossAmountList) {
        this.grossAmountList = grossAmountList;
    }

    public List<Double> getGrossAmountListAfter5PercentRecovery() {
        return grossAmountListAfter5PercentRecovery;
    }

    public void setGrossAmountListAfter5PercentRecovery(List<Double> grossAmountListAfter5PercentRecovery) {
        this.grossAmountListAfter5PercentRecovery = grossAmountListAfter5PercentRecovery;
    }

    public List<Integer> getLevelDifference() {
        return levelDifference;
    }

    public void setLevelDifference(List<Integer> levelDifference) {
        this.levelDifference = levelDifference;
    }

    public List<LargeAmountLoan> getLargeAmountLoanList() {
        return largeAmountLoanList;
    }

    public void setLargeAmountLoanList(List<LargeAmountLoan> largeAmountLoanList) {
        this.largeAmountLoanList = largeAmountLoanList;
    }
}
