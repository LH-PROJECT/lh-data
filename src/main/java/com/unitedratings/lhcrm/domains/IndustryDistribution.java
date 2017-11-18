package com.unitedratings.lhcrm.domains;

import java.util.HashSet;
import java.util.Set;

/**
 * 行业分布
 * @author wangyongxin
 * @createAt 2017-10-19 下午5:48
 **/
public final class IndustryDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{INDUSTRY,DEBT_NUM,LOAN_NUM,LOAN_BALANCE,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new IndustryStatistical();
    }

    /**
     * 行业统计
     */
    public class IndustryStatistical extends Statistical{

        private IndustryStatistical(){}

        private String industryName;
        private Integer debtNum;
        private Integer loanNum;
        private Double amount;
        private Double proportion;
        private Set<Long> borrowerSet = new HashSet<>();


        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
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

        public Set<Long> getBorrowerSet() {
            return borrowerSet;
        }

        public void setBorrowerSet(Set<Long> borrowerSet) {
            this.borrowerSet = borrowerSet;
        }
    }
}
