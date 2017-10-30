package com.unitedratings.lhcrm.core;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.business.CreditPortfolioRiskAnalysis;
import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.*;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.service.interfaces.*;
import com.unitedratings.lhcrm.utils.*;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 蒙特卡洛模拟结果收集统计器
 * @author wangyongxin
 */
public class AnalysisResultMerge implements Callable<PortfolioStatisticalResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisResultMerge.class);

    private AtomicInteger alreadyNum = new AtomicInteger(0);

    private SimulationRecord record;

    private int parallelNum = 4;

    private int threshold = 400000;

    private FileConfig config;

    public AnalysisResultMerge(SimulationRecord simulationRecord,int parallel,int threshold,FileConfig fileConfig){
        this.record = simulationRecord;
        this.config = fileConfig;
        if(parallel>1){
            this.parallelNum = parallel;
        }
        if(threshold>Constant.PRECISION){
            this.threshold = threshold;
        }
    }

    @Override
    public PortfolioStatisticalResult call() throws BusinessException {
        long t1 = System.currentTimeMillis();
        //1、预处理准备资产池信息
        AssetPool assetPool = prepareAssetPoolInfo();
        long t2 = System.currentTimeMillis();
        LOGGER.info("数据预处理过程消耗{}ms",t2-t1);
        Integer num = record.getNum() * 10000;
        //2、蒙特卡洛模拟
        AssetPoolInfo info = assetPool.getAssetPoolInfo();
        assetPool.getAssetPoolSummaryResult().getStatisticalResult().setSimulationTimes(num.longValue());
        FinalMonteResult result = monteCarloSimulation(info, num);
        long t3 = System.currentTimeMillis();
        LOGGER.info("模拟过程消耗{}ms",t3-t2);
        PortfolioStatisticalResult portfolioStatisticalResult = null;
        try {
            //3、处理合并结果并输出
            //3.1、计算目标违约概率、目标违约率、目标回收率、目标损失率
            MonteSummaryResult monteSummaryResult = AssetAnalysisUtil.processMonteResult(result, info, num, info.getWeightedAverageMaturity());
            //3.2、计算资产违约分布、损失分布
            PortfolioDefaultDistribution distribution = AssetAnalysisUtil.CalPortfolioDefaultDistribution(result,2,num);
            portfolioStatisticalResult = new PortfolioStatisticalResult();
            portfolioStatisticalResult.setMonteResult(result);
            portfolioStatisticalResult.setMonteSummaryResult(monteSummaryResult);
            portfolioStatisticalResult.setPortfolioDefaultDistribution(distribution);
            final double defaultRateMean = result.getSumDefaultRate() / num;
            portfolioStatisticalResult.setAverageDefaultRate(defaultRateMean);
            portfolioStatisticalResult.setStandardDeviation(MathUtil.calculateStandardDeviation(result.getDefaultRate(),num,defaultRateMean));
            portfolioStatisticalResult.setAverageRecoveryRate(result.getAverageRecoveryRate());
            portfolioStatisticalResult.setPortfolioId(record.getAttachableId());
            //3.3、结果输出至excel
            String filePath = ExcelUtil.outputPortfolioAnalysisResult(portfolioStatisticalResult,assetPool,num,this.config);
            portfolioStatisticalResult.setResultFilePath(filePath);
            //3.4、结果保存至数据库
            saveAnalysisResult(portfolioStatisticalResult);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            throw new BusinessException("000004","蒙特卡洛模拟记过处理输出过程异常",e);
        }
        return portfolioStatisticalResult;
    }

    /**
     * 资产池统计量（该方法必须在债项评级之后调用）
     * @param assetPool
     */
    private void statisticalAssetPool(AssetPool assetPool) {
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        if(!CollectionUtils.isEmpty(loanRecords)){
            double[] yearMaturity = assetPool.getAssetPoolInfo().getYearMaturity();
            Map<String,IndustryDistribution.IndustryStatistical> industryMap = new HashMap<>();
            Map<String,AreaDistribution.AreaStatistical> areaMap = new HashMap<>();
            Map<String,DebtorCreditRankDistribution.DebtorCreditRankStatistical> debtorCreditRankMap = new HashMap<>();
            Map<String,ResidualMaturityDistribution.ResidualMaturityStatistical> residualMaturityMap = new TreeMap<>(Comparator.naturalOrder());
            Map<String,GuaranteeModeDistribution.GuaranteeModeStatistical> guaranteeModeMap = new HashMap<>();
            Map<Long,DebtorDistribution.DebtorStatistical> debtorMap = new HashMap<>();
            Map<String,GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical> guaranteeCreditRankMap = new HashMap<>();
            Map<String,LoanCreditRankDistribution.LoanCreditRankStatistical> loanCreditRankMap = new HashMap<>();
            //首次结果分类统计
            double[] doubles = statisticalDistribution(loanRecords, yearMaturity, industryMap, areaMap, debtorCreditRankMap, residualMaturityMap, guaranteeModeMap, debtorMap, guaranteeCreditRankMap, loanCreditRankMap);
            Integer debtNum = new Double(doubles[0]).intValue();
            Double guaranteeAmount = doubles[1];
            //统计结果汇总、计算
            statisticsCollect(assetPool, industryMap, areaMap, debtorCreditRankMap, residualMaturityMap, guaranteeModeMap, debtorMap, guaranteeCreditRankMap, loanCreditRankMap, guaranteeAmount,debtNum);
            //其他统计结果计算
            surplusStatisticsCalculate(assetPool,debtNum);
        }
    }

    /**
     * 剩余资产池统计量计算
     * @param assetPool
     * @param debtNum
     */
    private void surplusStatisticsCalculate(AssetPool assetPool, Integer debtNum) {
        StatisticalResult statisticalResult = new StatisticalResult();
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        Portfolio portfolio = assetPool.getPortfolio();
        double[] principal = assetPoolInfo.getPrincipal();
        double sumPrincipal = StatUtils.sum(principal);
        double[] yearMaturity = assetPoolInfo.getYearMaturity();
        statisticalResult.setTransactionName(portfolio.getPortfolioName());
        statisticalResult.setBeginCalculateDate(portfolio.getBeginCalculateDate());
        statisticalResult.setAssetServiceInstitution(portfolio.getSponsorName());
        statisticalResult.setSponsorOrganization(portfolio.getPortfolioName());
        statisticalResult.setLoanNum(assetPoolInfo.getLoanNum());
        statisticalResult.setDebtorNum(debtNum);
        statisticalResult.setOutstandingPrincipal(sumPrincipal);
        statisticalResult.setWeightedYearMaturity(assetPoolInfo.getWeightedAverageMaturity());
        statisticalResult.setLongestMaturity(org.ujmp.core.util.MathUtil.max(yearMaturity));
        statisticalResult.setShortestMaturity(MathUtil.min(yearMaturity));
        //计算
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        double war = 0;
        double warLoanPD = 0;
        double war_g = 0;
        double war_c = 0;
        double war_r = 0;
        double interest = 0;
        double wase = 0;
        //double roundupM = 0;
        for(int i = 0; i< loanRecords.size(); i++){
            DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
            if(debtorInfo.getDefaultMagnification()!=null){
                war += principal[i]*assetPoolInfo.getAssetDR()[i]/debtorInfo.getDefaultMagnification();
            }else {
                war += principal[i]*assetPoolInfo.getAssetDR()[i];
            }
            warLoanPD += principal[i]*assetPoolInfo.getLoanPD()[i];
            war_g += principal[i]*debtorInfo.getGuaranteeRecoveryRate();
            war_c += principal[i]*debtorInfo.getMortgageRecoveryRate();
            war_r += principal[i]*debtorInfo.getFinalRecoveryRate();
            interest += principal[i]*debtorInfo.getLendingRate();
            wase += principal[i] * DateUtil.calculatePeriods(SummaryType.YEAR.getValue(),debtorInfo.getLoanProvideDate(),portfolio.getBeginCalculateDate());
            //roundupM += principal[i]*Math.ceil(yearMaturity[i]);
        }
        double war_s = assetPoolInfo.getWeightedDebtorSelfRecoverRate()/sumPrincipal;
        war = war/sumPrincipal;
        warLoanPD = warLoanPD/sumPrincipal;
        war_g = war_g/sumPrincipal;
        war_c = war_c/sumPrincipal;
        war_r = war_r/sumPrincipal;
        interest = interest/sumPrincipal/100;
        wase = wase/sumPrincipal;
        //roundupM = roundupM/sumPrincipal;
        statisticalResult.setWeightedAverageRecoverRate(war_r);
        statisticalResult.setWeightedDebtorDefaultRate(war);
        statisticalResult.setWeightedLoanDefaultRate(warLoanPD);
        statisticalResult.setAging(wase);
        statisticalResult.setWeightedAverageInterestRate(interest);
        statisticalResult.setWeightedDebtorSelfRecoverRate(war_s);
        statisticalResult.setWeightedGuaranteePromotedRecoverRate(war_g - war_s);
        statisticalResult.setWeightedCollateralAverageRecoverRate(war_c);
        int war1 = AssetAnalysisUtil.portfolioRating(assetPoolInfo.getPerfectDefaultRate(), assetPoolInfo.getWeightedAverageMaturity(), war);
        //int WARroundup = AssetAnalysisUtil.portfolioRating(assetPoolInfo.getPerfectDefaultRate(), roundupM, war);
        int WALoan = AssetAnalysisUtil.portfolioRating(assetPoolInfo.getPerfectDefaultRate(), assetPoolInfo.getWeightedAverageMaturity(),warLoanPD);
        //int WARLroundup = AssetAnalysisUtil.portfolioRating(assetPoolInfo.getPerfectDefaultRate(), roundupM,warLoanPD);
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        SysDictionaryServiceSV dictionaryService = applicationContext.getBean(SysDictionaryServiceSV.class);
        SysDictionary dict = dictionaryService.getDictByCodeAndVersion("CREDIT_LEVEL", 1.0);
        final List<SysDictionary> creditLevelList = dictionaryService.getDictionaryListByParentId(dict.getId());
        statisticalResult.setWeightedDebtorCreditLevel(AssetAnalysisUtil.numToCreditLevel(creditLevelList,war1));
        statisticalResult.setWeightedLoanCreditLevel(AssetAnalysisUtil.numToCreditLevel(creditLevelList,WALoan));
        assetPool.getAssetPoolSummaryResult().setStatisticalResult(statisticalResult);
    }

    /**
     * 统计结果汇总计算
     * @param assetPool
     * @param industryMap
     * @param areaMap
     * @param debtorCreditRankMap
     * @param residualMaturityMap
     * @param guaranteeModeMap
     * @param debtorMap
     * @param guaranteeCreditRankMap
     * @param loanCreditRankMap
     * @param guaranteeAmount
     * @param debtNum
     */
    private void statisticsCollect(AssetPool assetPool, Map<String, IndustryDistribution.IndustryStatistical> industryMap, Map<String, AreaDistribution.AreaStatistical> areaMap, Map<String, DebtorCreditRankDistribution.DebtorCreditRankStatistical> debtorCreditRankMap, Map<String, ResidualMaturityDistribution.ResidualMaturityStatistical> residualMaturityMap, Map<String, GuaranteeModeDistribution.GuaranteeModeStatistical> guaranteeModeMap, Map<Long, DebtorDistribution.DebtorStatistical> debtorMap, Map<String, GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical> guaranteeCreditRankMap, Map<String, LoanCreditRankDistribution.LoanCreditRankStatistical> loanCreditRankMap, Double guaranteeAmount, Integer debtNum) {
        double sumPrincipal = StatUtils.sum(assetPool.getAssetPoolInfo().getPrincipal());
        AssetPoolSummaryResult assetPoolSummaryResult = new AssetPoolSummaryResult();
        //行业汇总
        List<IndustryDistribution.IndustryStatistical> industryStatistics = new ArrayList<>();
        industryMap.forEach((key,statistical)->{
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            industryStatistics.add(statistical);
        });
        if(industryStatistics.size()>0){
            IndustryDistribution industryDistribution = new IndustryDistribution();
            IndustryDistribution.IndustryStatistical statical = (IndustryDistribution.IndustryStatistical) industryDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setIndustryName("合计");
            statical.setDebtNum(debtNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            industryStatistics.add(statical);
            industryDistribution.setDetails(industryStatistics);
            assetPoolSummaryResult.setIndustryDistribution(industryDistribution);
        }
        //地区汇总
        List<AreaDistribution.AreaStatistical>  areaStatistics= new ArrayList<>();
        areaMap.forEach((key,statistical)->{
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            areaStatistics.add(statistical);
        });
        if(areaStatistics.size()>0){
            AreaDistribution areaDistribution = new AreaDistribution();
            AreaDistribution.AreaStatistical statical = (AreaDistribution.AreaStatistical) areaDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setAreaName("合计");
            statical.setDebtNum(debtNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            areaStatistics.add(statical);
            areaDistribution.setDetails(areaStatistics);
            assetPoolSummaryResult.setAreaDistribution(areaDistribution);
        }
        //借款人信用等级汇总
        List<DebtorCreditRankDistribution.DebtorCreditRankStatistical>  debtorCreditRankStatistics = new ArrayList<>();
        debtorCreditRankMap.forEach((key,statistical)->{
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            debtorCreditRankStatistics.add(statistical);
        });
        if(debtorCreditRankStatistics.size()>0){
            DebtorCreditRankDistribution debtorCreditRankDistribution = new DebtorCreditRankDistribution();
            DebtorCreditRankDistribution.DebtorCreditRankStatistical statical = (DebtorCreditRankDistribution.DebtorCreditRankStatistical) debtorCreditRankDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setCreditLevel("合计");
            statical.setDebtNum(debtNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            debtorCreditRankStatistics.add(statical);
            debtorCreditRankDistribution.setDetails(debtorCreditRankStatistics);
            assetPoolSummaryResult.setDebtorCreditRankDistribution(debtorCreditRankDistribution);
        }
        //剩余期限汇总
        List<ResidualMaturityDistribution.ResidualMaturityStatistical>  residualMaturityStatistics= new ArrayList<>();
        int debtorNum = 0;
        for(ResidualMaturityDistribution.ResidualMaturityStatistical statistical:residualMaturityMap.values()){
            debtorNum += statistical.getBorrowerSet().size();
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            residualMaturityStatistics.add(statistical);
        }
        if(residualMaturityStatistics.size()>0){
            ResidualMaturityDistribution residualMaturityDistribution = new ResidualMaturityDistribution();
            ResidualMaturityDistribution.ResidualMaturityStatistical statical = (ResidualMaturityDistribution.ResidualMaturityStatistical) residualMaturityDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setResidualMaturity("合计");
            statical.setDebtNum(debtorNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            residualMaturityStatistics.add(statical);
            residualMaturityDistribution.setDetails(residualMaturityStatistics);
            assetPoolSummaryResult.setResidualMaturityDistribution(residualMaturityDistribution);
        }
        //担保方式汇总
        List<GuaranteeModeDistribution.GuaranteeModeStatistical>  guaranteeModeStatistics= new ArrayList<>();
        guaranteeModeMap.forEach((key,statistical)->{
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            guaranteeModeStatistics.add(statistical);
        });
        if(guaranteeModeStatistics.size()>0){
            GuaranteeModeDistribution guaranteeModeDistribution = new GuaranteeModeDistribution();
            GuaranteeModeDistribution.GuaranteeModeStatistical statical = (GuaranteeModeDistribution.GuaranteeModeStatistical) guaranteeModeDistribution.createStatical();
            statical.setGuaranteeMode("合计");
            statical.setDebtNum(debtNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            statical.setProportion(1.0);
            guaranteeModeStatistics.add(statical);
            guaranteeModeDistribution.setDetails(guaranteeModeStatistics);
            assetPoolSummaryResult.setGuaranteeModeDistribution(guaranteeModeDistribution);
        }
        //债务人汇总
        List<DebtorDistribution.DebtorStatistical>  debtorStatistics= new ArrayList<>();
        debtorMap.forEach((key,statistical)->{
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            debtorStatistics.add(statistical);
        });
        if(debtorStatistics.size()>0){
            DebtorDistribution debtorDistribution = new DebtorDistribution();
            DebtorDistribution.DebtorStatistical statical = (DebtorDistribution.DebtorStatistical) debtorDistribution.createStatical();
            statical.setLoanSerial("合计");
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            statical.setProportion(1.0);
            debtorStatistics.add(statical);
            debtorDistribution.setDetails(debtorStatistics);
            assetPoolSummaryResult.setDebtorDistribution(debtorDistribution);
        }
        //保证人信用等级汇总
        List<GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical>  guaranteeCreditRankStatistics= new ArrayList<>();
        int guaranteeNum = 0;
        int guaranteeLoanNum = 0;
        for(GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical statistical:guaranteeCreditRankMap.values()){
            guaranteeNum += statistical.getGuaranteeSet().size();
            guaranteeLoanNum += statistical.getGuaranteeLoanNum();
            statistical.setGuaranteeNum(statistical.getGuaranteeSet().size());
            statistical.setProportion(statistical.getAmount()/guaranteeAmount);
            guaranteeCreditRankStatistics.add(statistical);
        }
        if(guaranteeCreditRankStatistics.size()>0){
            GuaranteeCreditRankDistribution guaranteeCreditRankDistribution = new GuaranteeCreditRankDistribution();
            GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical statical = (GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical) guaranteeCreditRankDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setCreditLevel("合计");
            statical.setGuaranteeNum(guaranteeNum);
            statical.setAmount(guaranteeAmount);
            statical.setGuaranteeLoanNum(guaranteeLoanNum);
            guaranteeCreditRankStatistics.add(statical);
            guaranteeCreditRankDistribution.setDetails(guaranteeCreditRankStatistics);
            assetPoolSummaryResult.setGuaranteeCreditRankDistribution(guaranteeCreditRankDistribution);
        }
        //贷款信用等级汇总
        List<LoanCreditRankDistribution.LoanCreditRankStatistical>  loanCreditRankStatistics= new ArrayList<>();
        loanCreditRankMap.forEach((key,statistical)->{
            statistical.setDebtNum(statistical.getBorrowerSet().size());
            statistical.setProportion(statistical.getAmount()/sumPrincipal);
            loanCreditRankStatistics.add(statistical);
        });
        if(loanCreditRankStatistics.size()>0){
            LoanCreditRankDistribution loanCreditRankDistribution = new LoanCreditRankDistribution();
            LoanCreditRankDistribution.LoanCreditRankStatistical statical = (LoanCreditRankDistribution.LoanCreditRankStatistical) loanCreditRankDistribution.createStatical();
            statical.setProportion(1.0);
            statical.setCreditLevel("合计");
            statical.setDebtNum(debtNum);
            statical.setAmount(sumPrincipal);
            statical.setLoanNum(assetPool.getAssetPoolInfo().getLoanNum());
            loanCreditRankStatistics.add(statical);
            loanCreditRankDistribution.setDetails(loanCreditRankStatistics);
            assetPoolSummaryResult.setLoanCreditRankDistribution(loanCreditRankDistribution);
        }
        assetPool.setAssetPoolSummaryResult(assetPoolSummaryResult);
    }

    /**
     * 首次统计贷款记录
     * @param loanRecords
     * @param yearMaturity
     * @param industryMap
     * @param areaMap
     * @param debtorCreditRankMap
     * @param residualMaturityMap
     * @param guaranteeModeMap
     * @param debtorMap
     * @param guaranteeCreditRankMap
     * @param loanCreditRankMap
     * @return
     */
    private double[] statisticalDistribution(List<LoanRecord> loanRecords, double[] yearMaturity, Map<String, IndustryDistribution.IndustryStatistical> industryMap, Map<String, AreaDistribution.AreaStatistical> areaMap, Map<String, DebtorCreditRankDistribution.DebtorCreditRankStatistical> debtorCreditRankMap, Map<String, ResidualMaturityDistribution.ResidualMaturityStatistical> residualMaturityMap, Map<String, GuaranteeModeDistribution.GuaranteeModeStatistical> guaranteeModeMap, Map<Long, DebtorDistribution.DebtorStatistical> debtorMap, Map<String, GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical> guaranteeCreditRankMap, Map<String, LoanCreditRankDistribution.LoanCreditRankStatistical> loanCreditRankMap) {
        double[] arr = new double[2];
        double guaranteeAmount = 0;
        Set<Long> borrowerSet = new HashSet<>();
        for(int i=0;i<loanRecords.size();i++){
            LoanRecord loanRecord = loanRecords.get(i);
            DebtorInfo debtorInfo = loanRecord.getDebtorInfo();
            GuarantorInfo guarantorInfo = loanRecord.getGuarantorInfo();
            borrowerSet.add(debtorInfo.getBorrowerSerial());
            //行业分布
            String industryName = debtorInfo.getBorrowerIndustry();
            IndustryDistribution.IndustryStatistical industryStatistical = industryMap.get(industryName);
            if(industryStatistical == null){
                IndustryDistribution industryDistribution = new IndustryDistribution();
                industryStatistical = (IndustryDistribution.IndustryStatistical) industryDistribution.createStatical();
                industryStatistical.setIndustryName(industryName);
                industryStatistical.setAmount(debtorInfo.getLoanBalance());
                industryStatistical.setLoanNum(1);
                industryStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                industryStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                industryStatistical.setAmount(industryStatistical.getAmount()+ debtorInfo.getLoanBalance());
                industryStatistical.setLoanNum(industryStatistical.getLoanNum()+1);
            }
            industryMap.put(industryName,industryStatistical);

            //地区分布
            String areaName = debtorInfo.getBorrowerArea();
            AreaDistribution.AreaStatistical areaStatistical = areaMap.get(areaName);
            if(areaStatistical == null){
                AreaDistribution areaDistribution = new AreaDistribution();
                areaStatistical = (AreaDistribution.AreaStatistical) areaDistribution.createStatical();
                areaStatistical.setAreaName(areaName);
                areaStatistical.setAmount(debtorInfo.getLoanBalance());
                areaStatistical.setLoanNum(1);
                areaStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                areaStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                areaStatistical.setAmount(areaStatistical.getAmount()+ debtorInfo.getLoanBalance());
                areaStatistical.setLoanNum(areaStatistical.getLoanNum()+1);
            }
            areaMap.put(areaName,areaStatistical);

            //借款人信用等级分布
            String creditLevel = debtorInfo.getCreditLevel();
            DebtorCreditRankDistribution.DebtorCreditRankStatistical debtorCreditRankStatistical = debtorCreditRankMap.get(creditLevel);
            if(debtorCreditRankStatistical == null){
                DebtorCreditRankDistribution debtorCreditRankDistribution = new DebtorCreditRankDistribution();
                debtorCreditRankStatistical = (DebtorCreditRankDistribution.DebtorCreditRankStatistical) debtorCreditRankDistribution.createStatical();
                debtorCreditRankStatistical.setCreditLevel(creditLevel);
                debtorCreditRankStatistical.setAmount(debtorInfo.getLoanBalance());
                debtorCreditRankStatistical.setLoanNum(1);
                debtorCreditRankStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                debtorCreditRankStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                debtorCreditRankStatistical.setAmount(debtorCreditRankStatistical.getAmount()+ debtorInfo.getLoanBalance());
                debtorCreditRankStatistical.setLoanNum(debtorCreditRankStatistical.getLoanNum()+1);
            }
            debtorCreditRankMap.put(creditLevel,debtorCreditRankStatistical);

            //剩余期限分布
            double maturity = yearMaturity[i];
            String sectionKey = "("+(int)Math.floor(maturity)+","+(int)Math.ceil(maturity)+"]";
            ResidualMaturityDistribution.ResidualMaturityStatistical residualMaturityStatistical = residualMaturityMap.get(sectionKey);
            if(residualMaturityStatistical == null){
                ResidualMaturityDistribution residualMaturityDistribution = new ResidualMaturityDistribution();
                residualMaturityStatistical = (ResidualMaturityDistribution.ResidualMaturityStatistical) residualMaturityDistribution.createStatical();
                residualMaturityStatistical.setResidualMaturity(sectionKey);
                residualMaturityStatistical.setAmount(debtorInfo.getLoanBalance());
                residualMaturityStatistical.setLoanNum(1);
                residualMaturityStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                residualMaturityStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                residualMaturityStatistical.setAmount(residualMaturityStatistical.getAmount()+ debtorInfo.getLoanBalance());
                residualMaturityStatistical.setLoanNum(residualMaturityStatistical.getLoanNum()+1);
            }
            residualMaturityMap.put(sectionKey,residualMaturityStatistical);

            //担保方式分布
            String guaranteeMode = guarantorInfo.getGuaranteeMode();
            GuaranteeModeDistribution.GuaranteeModeStatistical guaranteeModeStatistical = guaranteeModeMap.get(guaranteeMode);
            if(guaranteeModeStatistical == null){
                GuaranteeModeDistribution guaranteeModeDistribution = new GuaranteeModeDistribution();
                guaranteeModeStatistical = (GuaranteeModeDistribution.GuaranteeModeStatistical) guaranteeModeDistribution.createStatical();
                guaranteeModeStatistical.setGuaranteeMode(guaranteeMode);
                guaranteeModeStatistical.setAmount(debtorInfo.getLoanBalance());
                guaranteeModeStatistical.setLoanNum(1);
                guaranteeModeStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                guaranteeModeStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                guaranteeModeStatistical.setAmount(guaranteeModeStatistical.getAmount()+ debtorInfo.getLoanBalance());
                guaranteeModeStatistical.setLoanNum(guaranteeModeStatistical.getLoanNum()+1);
            }
            guaranteeModeMap.put(guaranteeMode,guaranteeModeStatistical);

            //债务人分布
            Long borrowerSerial = debtorInfo.getBorrowerSerial();
            DebtorDistribution.DebtorStatistical debtorStatistical = debtorMap.get(borrowerSerial);
            if(debtorStatistical == null){
                DebtorDistribution debtorDistribution = new DebtorDistribution();
                debtorStatistical = (DebtorDistribution.DebtorStatistical) debtorDistribution.createStatical();
                debtorStatistical.setLoanSerial(String.valueOf(borrowerSerial));
                debtorStatistical.setAmount(debtorInfo.getLoanBalance());
                debtorStatistical.setAreaName(debtorInfo.getBorrowerArea());
                debtorStatistical.setCreditLevel(debtorInfo.getCreditLevel());
                debtorStatistical.setBorrower(debtorInfo.getBorrowerName());
                debtorStatistical.setIndustryName(debtorInfo.getBorrowerIndustry());
                debtorStatistical.setLoanNum(1);
            }else {
                debtorStatistical.setAmount(debtorStatistical.getAmount()+ debtorInfo.getLoanBalance());
                debtorStatistical.setLoanNum(debtorStatistical.getLoanNum()+1);
            }
            debtorMap.put(borrowerSerial,debtorStatistical);

            //保证人信用等级分布
            String guaranteeCreditLevel = guarantorInfo.getGuaranteeCreditLevel();
            if(!StringUtils.isEmpty(guaranteeCreditLevel)){
                guaranteeAmount += debtorInfo.getLoanBalance();
                GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical guaranteeCreditRankStatistical = guaranteeCreditRankMap.get(guaranteeCreditLevel);
                if(guaranteeCreditRankStatistical == null){
                    GuaranteeCreditRankDistribution guaranteeCreditRankDistribution = new GuaranteeCreditRankDistribution();
                    guaranteeCreditRankStatistical = (GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical) guaranteeCreditRankDistribution.createStatical();
                    guaranteeCreditRankStatistical.setCreditLevel(guaranteeCreditLevel);
                    guaranteeCreditRankStatistical.setAmount(debtorInfo.getLoanBalance());
                    guaranteeCreditRankStatistical.setGuaranteeLoanNum(1);
                    guaranteeCreditRankStatistical.getGuaranteeSet().add(guarantorInfo.getGuaranteeName());
                }else {
                    guaranteeCreditRankStatistical.getGuaranteeSet().add(guarantorInfo.getGuaranteeName());
                    guaranteeCreditRankStatistical.setAmount(guaranteeCreditRankStatistical.getAmount()+ debtorInfo.getLoanBalance());
                    guaranteeCreditRankStatistical.setGuaranteeLoanNum(guaranteeCreditRankStatistical.getGuaranteeLoanNum()+1);
                }
                guaranteeCreditRankMap.put(guaranteeCreditLevel,guaranteeCreditRankStatistical);
            }

            //贷款信用等级分布
            String debtLevel = debtorInfo.getDebtLevel();
            LoanCreditRankDistribution.LoanCreditRankStatistical loanCreditRankStatistical = loanCreditRankMap.get(debtLevel);
            if(loanCreditRankStatistical == null){
                LoanCreditRankDistribution loanCreditRankDistribution = new LoanCreditRankDistribution();
                loanCreditRankStatistical = (LoanCreditRankDistribution.LoanCreditRankStatistical) loanCreditRankDistribution.createStatical();
                loanCreditRankStatistical.setCreditLevel(debtLevel);
                loanCreditRankStatistical.setAmount(debtorInfo.getLoanBalance());
                loanCreditRankStatistical.setLoanNum(1);
                loanCreditRankStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
            }else {
                loanCreditRankStatistical.getBorrowerSet().add(debtorInfo.getBorrowerSerial());
                loanCreditRankStatistical.setAmount(loanCreditRankStatistical.getAmount()+ debtorInfo.getLoanBalance());
                loanCreditRankStatistical.setLoanNum(loanCreditRankStatistical.getLoanNum()+1);
            }
            loanCreditRankMap.put(debtLevel,loanCreditRankStatistical);
        }

        arr[0] = borrowerSet.size();
        arr[1] = guaranteeAmount;
        return arr;
    }

    private void saveAnalysisResult(PortfolioStatisticalResult statisticalResult) throws BusinessException {
        try {
            ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
            PortfolioAnalysisServiceSV analysisService = applicationContext.getBean(PortfolioAnalysisServiceSV.class);
            PortfolioAnalysisResult portfolioAnalysisResult = new PortfolioAnalysisResult();
            portfolioAnalysisResult.setResultFilePath(statisticalResult.getResultFilePath());
            portfolioAnalysisResult.setPortfolioId(statisticalResult.getPortfolioId());
            portfolioAnalysisResult.setStandardDeviation(statisticalResult.getStandardDeviation());
            portfolioAnalysisResult.setCreateTime(new Date());
            portfolioAnalysisResult.setAverageDefaultRate(statisticalResult.getAverageDefaultRate());
            portfolioAnalysisResult.setAverageRecoveryRate(statisticalResult.getAverageRecoveryRate());
            portfolioAnalysisResult.setPortfolioDefaultDistribution(JSON.toJSONString(statisticalResult.getPortfolioDefaultDistribution()));
            portfolioAnalysisResult.setMonteResult(JSON.toJSONString(statisticalResult.getMonteResult()));
            portfolioAnalysisResult.setMonteSummaryResult(JSON.toJSONString(statisticalResult.getMonteSummaryResult()));
            PortfolioAnalysisResult analysisResult = analysisService.saveAnalysisResult(portfolioAnalysisResult);
            record.setResultId(analysisResult.getId());
        } catch (Exception e) {
            LOGGER.error("保存蒙特卡洛模拟结果至数据库过程异常",e);
            throw new BusinessException("000003","模拟结果保存过程异常");
        }
    }

    /**
     * 通用计算数据准备
     * @param assetPool
     */
    private void dataPreparation(AssetPool assetPool) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        SysDictionaryServiceSV dictionaryService = applicationContext.getBean(SysDictionaryServiceSV.class);
        SysDictionary dict = dictionaryService.getDictByCodeAndVersion("CREDIT_LEVEL", 1.0);
        SysDictionary guaranteeMode = dictionaryService.getDictByCodeAndVersion("GUARANTEE_MODE", 1.0);
        if(dict!=null){
            Integer loanNum = assetPool.getAssetPoolInfo().getLoanNum();
            //借款人信用等级
            int[] numRating = new int[loanNum];
            //担保机构最高信用等级
            int[] scRating = new int[loanNum];
            //主权信用等级
            int[] sovRating = new int[loanNum];
            //借款人最高信用等级
            int[] loanRating = new int[loanNum];
            //1、量化等级
            final List<SysDictionary> creditLevelList = dictionaryService.getDictionaryListByParentId(dict.getId());
            final List<SysDictionary> guaranteeModeList = dictionaryService.getDictionaryListByParentId(guaranteeMode.getId());
            List<LoanRecord> loanRecords = assetPool.getLoanRecords();
            if(!CollectionUtils.isEmpty(loanRecords)){
                for(int i = 0;i<loanRecords.size();i++){
                    DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
                    GuarantorInfo guarantorInfo = loanRecords.get(i).getGuarantorInfo();
                    for(SysDictionary dictionary:creditLevelList){
                        int found = 0;
                        if(dictionary.getParamCode().equals(debtorInfo.getCreditLevel())){
                            debtorInfo.setCreditLevelCode(dictionary.getId());
                            numRating[i] = Integer.parseInt(dictionary.getParamValue());
                            found++;
                        }
                        if(dictionary.getParamCode().equals(debtorInfo.getSovereignCreditLevel())){
                            debtorInfo.setSovereignCreditLevelCode(dictionary.getId());
                            sovRating[i] = Integer.parseInt(dictionary.getParamValue());
                            found++;
                        }
                        if(dictionary.getParamCode().equals(guarantorInfo.getGuaranteeCreditLevel())){
                            guarantorInfo.setGuaranteeCreditLevelCode(dictionary.getId());
                            scRating[i] = Integer.parseInt(dictionary.getParamValue());
                            found++;
                        }else if("最低风险主权等级".equals(guarantorInfo.getGuaranteeCreditLevel())){
                            scRating[i]= 1;
                            found++;
                        }
                        if(found==3){
                            break;
                        }
                    }
                    //关联担保方式
                    for(SysDictionary dictionary:guaranteeModeList){
                        if(dictionary.getParamDesc().equals(guarantorInfo.getGuaranteeMode())){
                            guarantorInfo.setGuaranteeModeCode(dictionary.getParamCode());
                            guarantorInfo.setGuaranteeModeId(dictionary.getId());
                        }
                    }
                }
                for(int i=0;i<loanNum;i++){
                    if(scRating[i]==0){
                        loanRating[i] = numRating[i];
                    }else {
                        loanRating[i] = Math.min(numRating[i],scRating[i]);
                    }
                    SysDictionary dictionary = creditLevelList.get(loanRating[i] - 1);
                    DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
                    debtorInfo.setDebtLevel(dictionary.getParamCode());
                    debtorInfo.setDebtLevelCode(dictionary.getId());
                }
                //1、计算违约放大倍数
                AssetAnalysisUtil.calDefaultMagnification(assetPool,numRating,scRating,loanRating);
                //2、计算基础资产、担保人违约概率
                AssetAnalysisUtil.calAssetAndGuaranteeDefaultRate(assetPool,numRating,scRating,loanRating);
                //3、计算条件违约率
                AssetAnalysisUtil.calConditionDefaultRate(assetPool,numRating);
                //4、调整回收率
                AssetAnalysisUtil.adjustDefaultRate(assetPool,numRating);
            }
        }
    }

    /**
     * 蒙特卡洛模拟
     * @param info
     * @param num
     * @return
     */
    private FinalMonteResult monteCarloSimulation(AssetPoolInfo info, Integer num) throws BusinessException {
        MonteResult result = null;
        try {
            if(num<threshold){
                Future<MonteResult> future = ExecutorEngine.getExecutorEngine().submit(new CreditPortfolioRiskAnalysis(info, num, alreadyNum, Constant.PRECISION));
                result = future.get();
            }else {
                result = new MonteResult();
                List<CreditPortfolioRiskAnalysis> taskList = new ArrayList<>();
                if(num>=threshold*10){//超过400w次，并行数加倍
                    parallelNum *= 2;
                }
                for(int i=0;i<parallelNum;i++){
                    taskList.add(new CreditPortfolioRiskAnalysis(info, num/parallelNum, alreadyNum,Constant.PRECISION));
                }
                List<Future<MonteResult>> futures = ExecutorEngine.getExecutorEngine().invokeAll(taskList);
                double[] defaultRate = new double[Constant.PRECISION];
                double[] lossRate = new double[Constant.PRECISION];
                double[] recoveryRate = new double[Constant.PRECISION];
                //违约记录矩阵
                Matrix defaultRecord = null;
                //总违约金额
                double sumDefault = 0;
                double sumRecovery = 0;
                double sumDefaultRate = 0;
                for(Future<MonteResult> future:futures){
                    MonteResult monteResult = future.get();
                    for(int i=0;i<Constant.PRECISION;i++){
                        defaultRate[i] += monteResult.getDefaultRate()[i];
                        recoveryRate[i] += monteResult.getRecoveryRate()[i];
                        lossRate[i] += monteResult.getLossRate()[i];
                    }
                    if(defaultRecord==null){
                        defaultRecord = monteResult.getDefaultRecordMatrix();
                    }else {
                        defaultRecord = defaultRecord.plus(monteResult.getDefaultRecordMatrix());
                    }
                    sumDefault += monteResult.getSumDefault();
                    sumRecovery += monteResult.getSumRecovery();
                    sumDefaultRate += monteResult.getSumDefaultRate();
                }
                result.setDefaultRate(defaultRate);
                result.setRecoveryRate(recoveryRate);
                result.setLossRate(lossRate);
                result.setSumDefault(sumDefault);
                result.setSumRecovery(sumRecovery);
                result.setDefaultRecordMatrix(defaultRecord);
                result.setSumDefaultRate(sumDefaultRate);
            }

            if(result!=null){
                result.getDefaultRecordMatrix().setLabel("违约记录矩阵");
                //季度数
                Integer quarter = MathUtil.getMaxQuarter(info.getMaturity());
                //存放按季度的违约比率
                double[] defaultRateByPeriod = new double[quarter];
                double sumDefault = result.getSumDefault();
                double sumRecovery = result.getSumRecovery();
                Matrix defaultRecord = result.getDefaultRecordMatrix();
                //按季度计算违约比率
                if(result.getSumDefault() > 0 && defaultRecord !=null){
                    Integer loanNum = info.getLoanNum();
                    Matrix amortisation = info.getAmortisation();
                    double[] secureAmount = info.getSecureAmount();
                    double[] principal = info.getPrincipal();
                    double[] outamor = new double[loanNum];
                    for(int j=0;j<quarter;j++){
                        double vertAmount = 0;
                        for(int i = 0; i< loanNum; i++){
                            if(j>0){
                                outamor[i] += amortisation.getAsDouble(i,j-1);
                            }
                            vertAmount += defaultRecord.getAsDouble(i,j)*Math.max(principal[i]-outamor[i]-secureAmount[i],0);
                        }
                        defaultRateByPeriod[j] = vertAmount/sumDefault;
                    }
                }
                FinalMonteResult finalMonteResult = new FinalMonteResult();
                finalMonteResult.setDefaultRateByPeriod(defaultRateByPeriod);
                finalMonteResult.setDefaultRate(result.getDefaultRate());
                finalMonteResult.setRecoveryRate(result.getRecoveryRate());
                finalMonteResult.setLossRate(result.getLossRate());
                finalMonteResult.setDefaultRecordMatrix(defaultRecord);
                finalMonteResult.setSumDefault(sumDefault);
                finalMonteResult.setSumRecovery(sumRecovery);
                finalMonteResult.setAverageRecoveryRate(sumRecovery/sumDefault);
                finalMonteResult.setSumDefaultRate(result.getSumDefaultRate());
                return finalMonteResult;
            }
        } catch (InterruptedException|ExecutionException e) {
            LOGGER.error("蒙特卡洛模拟过程异常",e);
            throw new BusinessException("000001","蒙特卡洛模拟过程异常");
        } catch (Exception e){
            LOGGER.error("蒙特卡洛模拟结果处理过程异常",e);
            throw new BusinessException("000002","蒙特卡洛模拟结果处理过程异常");
        }

        return null;
    }

    /**
     * 封装资产池信息
     * @return
     */
    private AssetPool prepareAssetPoolInfo() throws BusinessException {
        AssetPool assetPool = null;
        try {
            //查询封装资产池信息
            assetPool = assembleAssetPool();
            AssetPoolInfo info = assetPool.getAssetPoolInfo();
            //封装理想违约率
            assembleIdealDefaultMatrix(info);
            //基础计算数据处理
            dataPreparation(assetPool);
            //汇总资产池统计量
            statisticalAssetPool(assetPool);
            //保存资产池信息
            saveAssetPool(assetPool);
        } catch (Exception e) {
            LOGGER.error("资产池信息预处理过程异常",e);
            throw new BusinessException("000005","资产池信息预处理过程异常");
        }
        return assetPool;
    }

    /**
     * 查询数据库封装资产池信息
     * @return
     */
    private AssetPool assembleAssetPool() {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        PortfolioServiceSV portfolioService = applicationContext.getBean(PortfolioServiceSV.class);
        Portfolio portfolio = portfolioService.getPortfolioById(record.getAttachableId());
        AssetPool assetPool = null;
        if(portfolio!=null){
            assetPool = new AssetPool();
            assetPool.setPortfolio(portfolio);
            assetPool.setLoanRecords(portfolio.getRecordList());
            AssetPoolInfo info = new AssetPoolInfo();
            info.setPortfolioName(portfolio.getPortfolioName());
            info.setLoanNum(portfolio.getRecordList().size());
            info.setSummaryType(record.getSummaryType());
            info.setBeginCalculateDate(portfolio.getBeginCalculateDate());
            //封装资产池信息
            assembleAssetPoolInfo(info,portfolio.getRecordList());
            //封装资产相关系数矩阵
            info.setCorrelation(assembleCorrelation(portfolio));
            //封装分期摊还信息
            assembleAmortisation(portfolio,info);
            assetPool.setAssetPoolInfo(info);
        }
        return assetPool;
    }

    /**
     * 封装分期摊还信息
     * @param portfolio
     * @param info
     */
    private void assembleAmortisation(Portfolio portfolio, AssetPoolInfo info) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        AmortizationServiceSV amortizationService = applicationContext.getBean(AmortizationServiceSV.class);
        List<AmortizationInfo> infoList = amortizationService.getAmortizationInfoListByPortfolioId(portfolio.getId());
        int loanNum = info.getLoanNum();
        Matrix amortizationMatrix = new DefaultDenseDoubleMatrix2D(loanNum,MathUtil.getMaxQuarter(info.getMaturity()));
        if(!CollectionUtils.isEmpty(infoList)){
            amortizationMatrix.setLabel("分期摊还信息矩阵");
            for(int i=0;i<loanNum;i++){
                AmortizationInfo amortizationInfo = infoList.get(i);
                String amortizationStr = amortizationInfo.getAmortization();
                if(!StringUtils.isEmpty(amortizationStr)){
                    String[] amortizationArr = amortizationStr.split(",");
                    int ceil = (int) Math.ceil(info.getMaturity()[i]);
                    for(int j=0;j<ceil;j++){
                        amortizationMatrix.setAsDouble(Double.parseDouble(amortizationArr[j]),i,j);
                    }
                }
            }
        }
        info.setAmortisation(amortizationMatrix);
    }

    /**
     * 封装资产相关系数矩阵
     * @param portfolio
     */
    private Matrix assembleCorrelation(Portfolio portfolio) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        SysDictionaryServiceSV dictionaryService = applicationContext.getBean(SysDictionaryServiceSV.class);
        SysDictionary dict = dictionaryService.getDictByCodeAndVersion("BASE_COEFFICIENT", 1.0);
        if(dict!=null){
            List<SysDictionary> assetCorrelationCoefficient = dictionaryService.getDictionaryListByParentId(dict.getId());
            if(!CollectionUtils.isEmpty(assetCorrelationCoefficient)){
                Map<String,Double> coefficientMap = new HashMap<>();
                for(SysDictionary dictionary:assetCorrelationCoefficient){
                    coefficientMap.put(dictionary.getParamCode(),Double.parseDouble(dictionary.getParamValue()));
                }
                return AssetAnalysisUtil.calCorrelationMatrix(portfolio,coefficientMap);
            }
        }
        return null;
    }

    /**
     * 封装AssetPoolInfo实体
     * @param info
     * @param recordList
     * @return
     */
    private void assembleAssetPoolInfo(AssetPoolInfo info, List<LoanRecord> recordList) {
        if(!CollectionUtils.isEmpty(recordList)){
            int loanNum = info.getLoanNum();
            long[] loanSerials = new long[loanNum];
            long[] borrowerSerials = new long[loanNum];
            double[] principal = new double[loanNum];
            double[] secureAmount = new double[loanNum];
            long[] industryCode = new long[loanNum];
            double[] maturity = new double[loanNum];
            double[] yearMaturity = new double[loanNum];
            for(int i=0;i<loanNum;i++){
                DebtorInfo debtorInfo = recordList.get(i).getDebtorInfo();
                loanSerials[i] = debtorInfo.getLoanSerial();
                borrowerSerials[i] = debtorInfo.getBorrowerSerial();
                principal[i] = debtorInfo.getLoanBalance();
                secureAmount[i] = debtorInfo.getDepositAmount();
                industryCode[i] = debtorInfo.getIndustryCode();
                maturity[i] = DateUtil.calculatePeriods(info.getSummaryType(),info.getBeginCalculateDate(),debtorInfo.getMaturityDate());
                yearMaturity[i] = DateUtil.calculatePeriods(SummaryType.YEAR.getValue(),info.getBeginCalculateDate(),debtorInfo.getMaturityDate());
            }
            info.setLoanSerial(loanSerials);
            info.setIds(borrowerSerials);
            info.setPrincipal(principal);
            info.setSecureAmount(secureAmount);
            info.setAssetType(industryCode);
            info.setMaturity(maturity);
            info.setYearMaturity(yearMaturity);
            info.setWeightedAverageMaturity(MathUtil.calculateWeightedAverageMaturity(principal,yearMaturity));
        }
    }


    /**
     * 保存资产池信息到数据库
     * @param assetPool
     */
    private void saveAssetPool(AssetPool assetPool) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        PortfolioServiceSV portfolioService = applicationContext.getBean(PortfolioServiceSV.class);
        Portfolio portfolio = new Portfolio();
        portfolio.setId(record.getAttachableId());
        portfolio.setIdealDefaultId(assetPool.getAssetPoolInfo().getIdealDefaultId());
        portfolio.setRecordList(assetPool.getLoanRecords());
        portfolioService.updatePortfolio(portfolio);
    }

    /**
     * 查询数据库封装理想违约率矩阵
     * @param info
     */
    private void assembleIdealDefaultMatrix(AssetPoolInfo info) {
        ApplicationContext context = SpringApplicationContextUtil.getContext();
        IdealServiceSV idealService = context.getBean(IdealServiceSV.class);
        IdealDefault idealDefault = idealService.getNewestIdealDefaultTable();
        if(idealDefault!=null){
            info.setIdealDefaultId(idealDefault.getId());
            List<IdealDefaultItem> defaultItems = idealService.getIdealDefaultItemListByIdealDefaultId(idealDefault.getId());
            if(!CollectionUtils.isEmpty(defaultItems)){
                int col = defaultItems.size()/10;
                Matrix perfectDefaultRate = new DefaultDenseDoubleMatrix2D(10,col);
                for(int i=0;i<10;i++){
                    for(int j=0;j < col;j++){
                        perfectDefaultRate.setAsDouble(defaultItems.get(i*col+j).getDefaultRate(),i,j);
                    }
                }
                perfectDefaultRate.setLabel("理想违约率表");
                info.setPerfectDefaultRate(perfectDefaultRate);
            }
        }
    }
}
