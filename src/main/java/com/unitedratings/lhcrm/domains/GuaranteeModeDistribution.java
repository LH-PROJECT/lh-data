package com.unitedratings.lhcrm.domains;

import java.util.HashSet;
import java.util.Set;

/**
 * 担保方式分布
 * @author wangyongxin
 * @createAt 2017-10-24 上午11:15
 **/
public final class GuaranteeModeDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{GUARANTEE_MODE,DEBT_NUM,LOAN_NUM,LOAN_BALANCE,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new GuaranteeModeStatistical();
    }

    public class GuaranteeModeStatistical extends Statistical{

        private GuaranteeModeStatistical(){}

        private String guaranteeMode;
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

        public String getGuaranteeMode() {
            return guaranteeMode;
        }

        public void setGuaranteeMode(String guaranteeMode) {
            this.guaranteeMode = guaranteeMode;
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
