package com.unitedratings.lhcrm.core;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.business.CreditPortfolioRiskAnalysis;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.domains.*;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.entity.UploadRecord;
import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import com.unitedratings.lhcrm.utils.AssetAnalysisUtil;
import com.unitedratings.lhcrm.utils.ExcelUtil;
import com.unitedratings.lhcrm.utils.MathUtil;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 蒙特卡洛模拟结果收集统计器
 */
public class AnalysisResultMerge implements Callable<PortfolioStatisticalResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisResultMerge.class);

    private AtomicInteger alreadyNum = new AtomicInteger(0);

    private UploadRecord record;

    private int parallelNum = 4;

    private int threshold = 400000;

    public AnalysisResultMerge(UploadRecord uploadRecord,int parallel,int threshold){
        this.record = uploadRecord;
        if(parallel>1){
            this.parallelNum = parallel;
        }
        if(threshold>Constant.PRECISION){
            this.threshold = threshold;
        }
    }

    @Override
    public PortfolioStatisticalResult call() throws Exception {
        long t1 = System.currentTimeMillis();
        AssetPoolInfo info = assetPoolExcelProcess();
        long t2 = System.currentTimeMillis();
        LOGGER.info("excel处理过程消耗{}ms",t2-t1);
        Integer num = record.getNum();
        MonteResult result = monteCarloSimulation(info, num);
        long t3 = System.currentTimeMillis();
        LOGGER.info("模拟过程消耗{}ms",t3-t2);
        //处理合并结果并输出
        MonteSummaryResult monteSummaryResult = AssetAnalysisUtil.processMonteResult(result, info, num, info.getWeightedAverageMaturity());
        PortfolioDefaultDistribution distribution = AssetAnalysisUtil.CalPortfolioDefaultDistribution(result,2,num);
        PortfolioStatisticalResult portfolioStatisticalResult = new PortfolioStatisticalResult();
        portfolioStatisticalResult.setMonteResult(result);
        portfolioStatisticalResult.setMonteSummaryResult(monteSummaryResult);
        portfolioStatisticalResult.setPortfolioDefaultDistribution(distribution);
        portfolioStatisticalResult.setStandardDeviation(MathUtil.calculateStandardDeviation(result.getDefaultRate(),num));
        portfolioStatisticalResult.setAverageDefaultRate(StatUtils.sum(result.getDefaultRate(),1,result.getDefaultRate().length - 1)/num);
        portfolioStatisticalResult.setUploadRecordId(record.getId());
        //结果输出至excel
        String filePath = ExcelUtil.outputPortfolioAnalysisResult(portfolioStatisticalResult,info,num);
        portfolioStatisticalResult.setResultFilePath(filePath);
        return portfolioStatisticalResult;
    }

    /**
     * 蒙特卡洛模拟
     * @param info
     * @param num
     * @return
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    private MonteResult monteCarloSimulation(AssetPoolInfo info, Integer num) throws InterruptedException, java.util.concurrent.ExecutionException {
        MonteResult result = null;
        if(num<threshold){
            Future<MonteResult> future = ExecutorEngine.getExecutorEngine().submit(new CreditPortfolioRiskAnalysis(info, num, alreadyNum, Constant.PRECISION));
            result = future.get();
        }else {
            result = new MonteResult();
            List<CreditPortfolioRiskAnalysis> taskList = new ArrayList<>();
            for(int i=0;i<parallelNum;i++){
                taskList.add(new CreditPortfolioRiskAnalysis(info, num/parallelNum, alreadyNum,Constant.PRECISION));
            }
            List<Future<MonteResult>> futures = ExecutorEngine.getExecutorEngine().invokeAll(taskList);
            double[] defaultRate = new double[Constant.PRECISION];
            double[] lossRate = new double[Constant.PRECISION];
            double[] recoveryRate = new double[Constant.PRECISION];
            double[] defaultRateByPeriod = null;
            for(Future<MonteResult> future:futures){
                MonteResult monteResult = future.get();
                for(int i=0;i<Constant.PRECISION;i++){
                    defaultRate[i] += monteResult.getDefaultRate()[i];
                    recoveryRate[i] += monteResult.getRecoveryRate()[i];
                    lossRate[i] += monteResult.getLossRate()[i];
                }
                if(defaultRateByPeriod==null){
                    defaultRateByPeriod = monteResult.getDefaultRateByPeriod();
                }else {
                    for(int i=0;i<defaultRateByPeriod.length;i++){
                        defaultRateByPeriod[i] += monteResult.getDefaultRateByPeriod()[i];
                    }
                }
            }
            for(int i=0;i<defaultRateByPeriod.length;i++){
                defaultRateByPeriod[i]=defaultRateByPeriod[i]/parallelNum;
            }
            result.setDefaultRate(defaultRate);
            result.setRecoveryRate(recoveryRate);
            result.setLossRate(lossRate);
            result.setDefaultRateByPeriod(defaultRateByPeriod);
        }
        return result;
    }

    /**
     * 资产池excel处理
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    private AssetPoolInfo assetPoolExcelProcess() throws InvalidFormatException, IOException {
        File file = new File(Constant.UPLOAD_PATH + File.separator + record.getFileName());
        AssetPool assetPool = new AssetPool();
        AssetPoolInfo info = new AssetPoolInfo();
        info.setReservesMoney(record.getReservesMoney());
        info.setBeginCalculateDate(record.getBeginCalculateDate());
        if(record.getSummaryType()!=null){
            info.setSummaryType(record.getSummaryType());
        }
        assetPool.setAssetPoolInfo(info);
        AssetsExcelProcess.processAssetsExcel(file,assetPool);
        return info;
    }
}
