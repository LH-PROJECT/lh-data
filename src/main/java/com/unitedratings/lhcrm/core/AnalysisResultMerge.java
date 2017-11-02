package com.unitedratings.lhcrm.core;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.business.CreditPortfolioRiskAnalysis;
import com.unitedratings.lhcrm.business.PortfolioDataCalculate;
import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.domains.*;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import com.unitedratings.lhcrm.utils.AssetAnalysisUtil;
import com.unitedratings.lhcrm.utils.ExcelUtil;
import com.unitedratings.lhcrm.utils.MathUtil;
import com.unitedratings.lhcrm.utils.SpringApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.ujmp.core.Matrix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        AssetPool assetPool = pretreatmentAssetPool();
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
     * 资产池预处理
     * @return
     * @throws BusinessException
     */
    private AssetPool pretreatmentAssetPool() throws BusinessException {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        PortfolioDataCalculate portfolioDataCalculate = applicationContext.getBean(PortfolioDataCalculate.class);
        AssetPool assetPool = portfolioDataCalculate.prepareAssetPoolInfo(record.getAttachableId(),record.getSummaryType());
        portfolioDataCalculate.statisticalAssetPool(assetPool);
        return assetPool;
    }

    /**
     * 保存资产池蒙特卡洛模拟结果
     * @param statisticalResult
     * @throws BusinessException
     */
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
}
