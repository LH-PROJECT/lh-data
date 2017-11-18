package com.unitedratings.lhcrm.domains;

import java.util.HashSet;
import java.util.Set;

/**
 * 借款人信用等级分布
 * @author wangyongxin
 * @createAt 2017-10-24 上午10:38
 **/
public final class DebtorCreditRankDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{CREDIT_LEVEL,DEBT_NUM,LOAN_NUM,LOAN_BALANCE,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new DebtorCreditRankStatistical();
    }

    public class DebtorCreditRankStatistical extends Statistical{

        private DebtorCreditRankStatistical(){}

        private String creditLevel;
        private Integer debtNum;
        private Integer loanNum;
        private Double amount;
        private Double proportion;
        private Set<Long> borrowerSet = new HashSet<>();

        public Set<Long> getBorrowerSet() {
            return borrowerSet;
        }

        public void setBorrowerSet(Set<Long> borrowerSet) {
            this.borrowerSet = borrowerSet;
        }

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
        }

        public Integer getDebtNum() {
            return debtNum;
        }

        public void setDebtNum(Integer debtNum) {
            this.debtNum = debtNum;
        }

        public Integer getLoanNum() {
            return loanNum;
        }

        public void setLoanNum(Integer loanNum) {
            this.loanNum = loanNum;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Double getProportion() {
            return proportion;
        }

        public void setProportion(Double proportion) {
            this.proportion = proportion;
        }
    }
}
