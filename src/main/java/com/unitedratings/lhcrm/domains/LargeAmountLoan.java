package com.unitedratings.lhcrm.domains;

import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-10-31 上午10:19
 **/
public class LargeAmountLoan {

    private Integer obligorSerial;
    private String debtorCreditLevel;
    private Integer debtorCreditLevelCode;
    private Double loanAmount;
    private Double collateralRecoveryRate;
    private List<Double> amountList;

    public Integer getObligorSerial() {
        return obligorSerial;
    }

    public void setObligorSerial(Integer obligorSerial) {
        this.obligorSerial = obligorSerial;
    }

    public String getDebtorCreditLevel() {
        return debtorCreditLevel;
    }

    public void setDebtorCreditLevel(String debtorCreditLevel) {
        this.debtorCreditLevel = debtorCreditLevel;
    }

    public Integer getDebtorCreditLevelCode() {
        return debtorCreditLevelCode;
    }

    public void setDebtorCreditLevelCode(Integer debtorCreditLevelCode) {
        this.debtorCreditLevelCode = debtorCreditLevelCode;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getCollateralRecoveryRate() {
        return collateralRecoveryRate;
    }

    public void setCollateralRecoveryRate(Double collateralRecoveryRate) {
        this.collateralRecoveryRate = collateralRecoveryRate;
    }

    public List<Double> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<Double> amountList) {
        this.amountList = amountList;
    }
}
