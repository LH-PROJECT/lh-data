package com.unitedratings.lhcrm.domains;

import java.util.HashSet;
import java.util.Set;

/**
 * 区域分布
 * @author wangyongxin
 * @createAt 2017-10-23 下午5:52
 **/
public final class AreaDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{AREA,DEBT_NUM,LOAN_NUM,LOAN_BALANCE,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new AreaStatistical();
    }

    public class AreaStatistical extends Statistical{
        private AreaStatistical(){}
        private String areaName;
        private Integer debtNum;
        private Integer loanNum;
        private Double amount;
        private Double proportion;
        private Set<Long> borrowerSet = new HashSet<>();

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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
