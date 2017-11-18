package com.unitedratings.lhcrm.domains;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangyongxin
 * @createAt 2017-10-24 下午2:52
 **/
public class GuaranteeCreditRankDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{CREDIT_LEVEL,GUARANTEE_NUM,GUARANTEE_LOAN_NUM,OUTSTANDING_LOAN_AMOUNT,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new GuaranteeCreditRankStatistical();
    }

    public class GuaranteeCreditRankStatistical extends Statistical{

        private GuaranteeCreditRankStatistical(){}

        private String creditLevel;
        private Integer GuaranteeNum;
        private Integer GuaranteeLoanNum;
        private Double amount;
        private Double proportion;
        private Set<String> GuaranteeSet = new HashSet<>();

        public Set<String> getGuaranteeSet() {
            return GuaranteeSet;
        }

        public void setGuaranteeSet(Set<String> guaranteeSet) {
            GuaranteeSet = guaranteeSet;
        }

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
        }

        public Integer getGuaranteeNum() {
            return GuaranteeNum;
        }

        public void setGuaranteeNum(Integer guaranteeNum) {
            GuaranteeNum = guaranteeNum;
        }

        public Integer getGuaranteeLoanNum() {
            return GuaranteeLoanNum;
        }

        public void setGuaranteeLoanNum(Integer guaranteeLoanNum) {
            GuaranteeLoanNum = guaranteeLoanNum;
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
