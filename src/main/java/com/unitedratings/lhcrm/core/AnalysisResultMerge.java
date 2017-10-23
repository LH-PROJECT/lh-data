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
        AssetPoolInfo info = prepareAssetPoolInfo();
        long t2 = System.currentTimeMillis();
        LOGGER.info("数据预处理过程消耗{}ms",t2-t1);
        Integer num = record.getNum() * 10000;
        //2、蒙特卡洛模拟
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
            portfolioStatisticalResult.setStandardDeviation(MathUtil.calculateStandardDeviation(result.getDefaultRate(),num));
            portfolioStatisticalResult.setAverageDefaultRate(result.getSumDefaultRate()/num);
            portfolioStatisticalResult.setPortfolioId(record.getAttachableId());
            //3.3、结果输出至excel
            String filePath = ExcelUtil.outputPortfolioAnalysisResult(portfolioStatisticalResult,info,num,this.config);
            portfolioStatisticalResult.setResultFilePath(filePath);
            //3.4、结果保存至数据库
            saveAnalysisResult(portfolioStatisticalResult);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            throw new BusinessException("000004","蒙特卡洛模拟记过处理输出过程异常");
        }
        return portfolioStatisticalResult;
    }

    private void statisticalAssetPool(AssetPool assetPool) {
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        if(!CollectionUtils.isEmpty(loanRecords)){
            Map<String,IndustryDistribution.IndustryStatical> map = new HashMap<>();
            for(int i=0;i<loanRecords.size();i++){
                LoanRecord loanRecord = loanRecords.get(i);
                String industryName = loanRecord.getDebtorInfo().getBorrowerIndustry();
                IndustryDistribution.IndustryStatical industryStatical = map.get(industryName);
                if(industryStatical == null){
                    industryStatical = IndustryDistribution.createIndustryStatical();
                    industryStatical.setIndustryName(industryName);
                }
                industryStatical.setAmount(loanRecord.getDebtorInfo().getLoanBalance());
                industryStatical.setDebtNum(1);
                industryStatical.setLoanNum(1);
                map.put(industryName,industryStatical);
            }
        }
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
                    sumDefaultRate += monteResult.getSumDefaultRate();
                }
                result.setDefaultRate(defaultRate);
                result.setRecoveryRate(recoveryRate);
                result.setLossRate(lossRate);
                result.setSumDefault(sumDefault);
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
    private AssetPoolInfo prepareAssetPoolInfo() throws BusinessException {
        AssetPoolInfo info = null;
        try {
            //查询封装资产池信息
            AssetPool assetPool = assembleAssetPool();
            info = assetPool.getAssetPoolInfo();
            //汇总资产池统计量
            //statisticalAssetPool(assetPool);
            //封装理想违约率
            assembleIdealDefaultMatrix(info);
            //基础计算数据处理
            dataPreparation(assetPool);
            //保存资产池信息
            saveAssetPool(assetPool);
        } catch (Exception e) {
            LOGGER.error("资产池信息预处理过程异常",e);
            throw new BusinessException("000005","资产池信息预处理过程异常");
        }
        return info;
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
