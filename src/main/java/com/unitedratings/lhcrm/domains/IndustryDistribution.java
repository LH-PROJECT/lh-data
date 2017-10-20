package com.unitedratings.lhcrm.domains;

import java.util.List;

/**
 * 行业分布
 * @author wangyongxin
 * @createAt 2017-10-19 下午5:48
 **/
public class IndustryDistribution extends Distribution{
    private static final String[] HEADER = new String[]{"行业名称","借款人户数","贷款笔数","贷款余额（万元）","金额占比"};
    private List<IndustryStatical> details;

    public static String[] getHEADER() {
        return HEADER;
    }

    public List<IndustryStatical> getDetails() {
        return details;
    }

    public void setDetails(List<IndustryStatical> details) {
        this.details = details;
    }

    public static IndustryStatical createIndustryStatical(){
        return createIndustryStaticalInternal();
    }

    private static IndustryStatical createIndustryStaticalInternal() {
        return new IndustryStatical();
    }

    /**
     * 行业统计
     */
    public static class IndustryStatical{

        protected IndustryStatical(){}

        private String industryName;
        private Integer debtNum;
        private Integer loanNum;
        private Double amount;
        private Double proportion;


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
    }
}
