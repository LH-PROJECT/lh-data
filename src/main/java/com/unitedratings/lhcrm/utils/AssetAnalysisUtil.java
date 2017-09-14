package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.MonteResult;
import com.unitedratings.lhcrm.domains.MonteSummaryResult;
import com.unitedratings.lhcrm.domains.PortfolioDefaultDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.ujmp.core.Matrix;

public class AssetAnalysisUtil {

    private AssetAnalysisUtil(){}

    /**
     * 按年切分计算目标违约相关值
     * @param result 蒙特卡洛模拟结果
     * @param info 资产池实体
     * @param num 模拟次数
     * @param year 年限
     * @return
     */
    public static MonteSummaryResult processMonteResult(MonteResult result, AssetPoolInfo info, Integer num, double year) {
        //理想违约率矩阵
        Matrix perfectDefaultRate = info.getPerfectDefaultRate();
        //利用插值法，求出债券的到期年限对应的违约率
        int floor = (int) Math.floor(year);
        int ceil = (int) Math.ceil(year);
        int length = new Long(perfectDefaultRate.getColumnCount()).intValue();
        double[] targetDefaultProbability = new double[length];
        double[] targetDefaultRate = new double[length];
        double[] targetRecoveryRate = new double[length];
        double[] targetLossRate = new double[length];
        for(int i=0;i<length;i++){
            if(year<=1){
                targetDefaultProbability[i] = perfectDefaultRate.getAsDouble(ceil,i);
            }else {
                double defaultRateLow = perfectDefaultRate.getAsDouble(floor, i);
                double defaultRateUp = perfectDefaultRate.getAsDouble(ceil, i);
                if(defaultRateLow == defaultRateUp){
                    targetDefaultProbability[i] = defaultRateUp;
                }else {
                    targetDefaultProbability[i] = defaultRateLow + (year-floor)*(defaultRateUp-defaultRateLow)/(ceil-floor);
                }
            }
            int position = Math.max((int) Math.floor(num * targetDefaultProbability[i]),1);
            targetDefaultRate[i] =  (double) MathUtil.getProbabilityIndex(result.getDefaultRate(),position)/Constant.PRECISION;
            targetRecoveryRate[i] = (double) MathUtil.getProbabilityIndex(result.getRecoveryRate(),position)/Constant.PRECISION;
            targetLossRate[i] = (double) MathUtil.getProbabilityIndex(result.getLossRate(),position)/Constant.PRECISION;
        }
        MonteSummaryResult monteSummaryResult = new MonteSummaryResult();
        monteSummaryResult.setTargetDefaultProbability(targetDefaultProbability);
        monteSummaryResult.setTargetDefaultRate(targetDefaultRate);
        monteSummaryResult.setTargetRecoveryRate(targetRecoveryRate);
        monteSummaryResult.setTargetLossRate(targetLossRate);

        return monteSummaryResult;
    }


    /**
     * 计算组合违约分布
     * @param result 蒙特卡洛模拟结果
     * @param interval 模拟次数
     * @param num 间隔
     * @return
     */
    public static PortfolioDefaultDistribution CalPortfolioDefaultDistribution(MonteResult result, int interval, int num) {
        int size = result.getDefaultRate().length / (interval*100) + 1;
        double[] defaultDistribution = new double[size];
        double[] lossDistribution = new double[size];
        defaultDistribution[0] = result.getDefaultRate()[0]/num;
        lossDistribution[0] = result.getLossRate()[0]/num;
        for(int i = 1;i<size;i++){
            if(i==size-1){
                defaultDistribution[i] = StatUtils.sum(result.getDefaultRate(),(i-1)*interval*100+1,interval*100-1)/num;
                lossDistribution[i] = StatUtils.sum(result.getLossRate(),(i-1)*interval*100+1,interval*100-1)/num;
            }else {
                defaultDistribution[i] = StatUtils.sum(result.getDefaultRate(),(i-1)*interval*100+1,interval*100)/num;
                lossDistribution[i] = StatUtils.sum(result.getLossRate(),(i-1)*interval*100+1,interval*100)/num;
            }
        }
        PortfolioDefaultDistribution distribution = new PortfolioDefaultDistribution();
        distribution.setDefaultProbability(defaultDistribution);
        distribution.setLossProbability(lossDistribution);
        return distribution;
    }
}
