package com.unitedratings.lhcrm.domains;

/**
 * 债务人分布
 * @author wangyongxin
 * @createAt 2017-10-24 上午11:18
 **/
public final class DebtorDistribution extends Distribution{

    @Override
    protected String[] getHeader() {
        return new String[]{DEBTOR_SERIAL,BORROWER,LOAN_NUM,CREDIT_LEVEL,BELONG_INDUSTRY,BELONG_AREA,LOAN_AMOUNT,PROPORTION};
    }

    @Override
    protected Statistical createStatisticalInternal() {
        return new DebtorStatistical();
    }

    public class DebtorStatistical extends Statistical{

        private DebtorStatistical(){}

        private String loanSerial;
        private String borrower;
        private Integer loanNum;
        private String creditLevel;
        private String industryName;
        private String areaName;
        private Double amount;
        private Double proportion;

        public String getLoanSerial() {
            return loanSerial;
        }

        public void setLoanSerial(String loanSerial) {
            this.loanSerial = loanSerial;
        }

        public String getBorrower() {
            return borrower;
        }

        public void setBorrower(String borrower) {
            this.borrower = borrower;
        }

        public Integer getLoanNum() {
            return loanNum;
        }

        public void setLoanNum(Integer loanNum) {
            this.loanNum = loanNum;
        }

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
        }

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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
