package com.unitedratings.lhcrm.domains;

/**
 * 资产池统计结果汇总实体（包含资产池统计量及各维度汇总分布）
 * @author wangyongxin
 * @createAt 2017-10-19 下午5:29
 **/
public class AssetPoolSummaryResult {

    /**
     * 资产池统计量
     */
    private StatisticalResult statisticalResult;

    /**
     * 行业分布
     */
    private IndustryDistribution industryDistribution;

    /**
     * 地区分布
     */
    private AreaDistribution areaDistribution;

    /**
     * 借款人信用等级分布
     */
    private DebtorCreditRankDistribution debtorCreditRankDistribution;

    /**
     * 剩余期限分布
     */
    private ResidualMaturityDistribution residualMaturityDistribution;

    /**
     * 担保方式分布
     */
    private GuaranteeModeDistribution guaranteeModeDistribution;

    /**
     * 债务人分布
     */
    private DebtorDistribution debtorDistribution;

    /**
     * 保证人信用等级分布
     */
    private GuaranteeCreditRankDistribution guaranteeCreditRankDistribution;

    /**
     * 贷款信用等级分布
     */
    private LoanCreditRankDistribution loanCreditRankDistribution;

    public StatisticalResult getStatisticalResult() {
        return statisticalResult;
    }

    public void setStatisticalResult(StatisticalResult statisticalResult) {
        this.statisticalResult = statisticalResult;
    }

    public IndustryDistribution getIndustryDistribution() {
        return industryDistribution;
    }

    public void setIndustryDistribution(IndustryDistribution industryDistribution) {
        this.industryDistribution = industryDistribution;
    }

    public AreaDistribution getAreaDistribution() {
        return areaDistribution;
    }

    public void setAreaDistribution(AreaDistribution areaDistribution) {
        this.areaDistribution = areaDistribution;
    }

    public DebtorCreditRankDistribution getDebtorCreditRankDistribution() {
        return debtorCreditRankDistribution;
    }

    public void setDebtorCreditRankDistribution(DebtorCreditRankDistribution debtorCreditRankDistribution) {
        this.debtorCreditRankDistribution = debtorCreditRankDistribution;
    }

    public ResidualMaturityDistribution getResidualMaturityDistribution() {
        return residualMaturityDistribution;
    }

    public void setResidualMaturityDistribution(ResidualMaturityDistribution residualMaturityDistribution) {
        this.residualMaturityDistribution = residualMaturityDistribution;
    }

    public GuaranteeModeDistribution getGuaranteeModeDistribution() {
        return guaranteeModeDistribution;
    }

    public void setGuaranteeModeDistribution(GuaranteeModeDistribution guaranteeModeDistribution) {
        this.guaranteeModeDistribution = guaranteeModeDistribution;
    }

    public DebtorDistribution getDebtorDistribution() {
        return debtorDistribution;
    }

    public void setDebtorDistribution(DebtorDistribution debtorDistribution) {
        this.debtorDistribution = debtorDistribution;
    }

    public GuaranteeCreditRankDistribution getGuaranteeCreditRankDistribution() {
        return guaranteeCreditRankDistribution;
    }

    public void setGuaranteeCreditRankDistribution(GuaranteeCreditRankDistribution guaranteeCreditRankDistribution) {
        this.guaranteeCreditRankDistribution = guaranteeCreditRankDistribution;
    }

    public LoanCreditRankDistribution getLoanCreditRankDistribution() {
        return loanCreditRankDistribution;
    }

    public void setLoanCreditRankDistribution(LoanCreditRankDistribution loanCreditRankDistribution) {
        this.loanCreditRankDistribution = loanCreditRankDistribution;
    }
}
