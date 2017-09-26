package com.unitedratings.lhcrm.algorithm;

import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.MonteResult;
import com.unitedratings.lhcrm.utils.MathUtil;
import com.unitedratings.lhcrm.utils.MatrixUtil;
import org.apache.commons.math3.stat.StatUtils;
import org.ujmp.core.Matrix;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 蒙特卡洛模拟算法
 */
public class MonteCarlo {

    private MonteCarlo(){}
    /**
     * 多阶段按季度蒙特卡罗模拟+按季度分期摊还
     * @param assetPoolInfo 资产池信息
     * @param cov 资产相关系数矩阵cholesky分解矩阵
     * @param conInvM 理想按季度随机条件违约概率逆矩阵
     * @param rr 最终回收率
     * @param num 模拟次数
     * @param alreadyNum 已经模拟次数
     * @param precision 违约分布统计精确度，10000表示精确到小数点后4位
     */
    public static MonteResult AmortizedM(AssetPoolInfo assetPoolInfo, Matrix cov, Matrix conInvM, double[] rr, Integer num, AtomicInteger alreadyNum, Integer precision) {
        //获取到期期限
        double[] maturity = assetPoolInfo.getMaturity();
        //获取季度数
        Integer quarterNum = MathUtil.getMaxQuarter(maturity);
        //获取贷款笔数
        Integer loanNum = assetPoolInfo.getLoanNum();
        //贷款金额
        double[] principal = assetPoolInfo.getPrincipal();
        //保证金
        double[] secureAmount = assetPoolInfo.getSecureAmount();
        //违约次数记录存储矩阵
        Matrix record = Matrix.Factory.zeros(loanNum,quarterNum);
        //存放违约率
        double[] defaultRate = new double[precision];
        //存放回收率
        double[] recoveryRate = new double[precision];
        //存放损失率
        double[] lossRate = new double[precision];
        //存放按季度的违约比率
        double[] defaultRateByPeriod = new double[quarterNum];
        Matrix amortisation = assetPoolInfo.getAmortisation();
        final double totalPrincipal = StatUtils.sum(principal);
        double reservesMoney = assetPoolInfo.getReservesMoney();
        double sumDefault = 0;//总违约金额
        double sumRecovery = 0;//总回收金额
        int count = 0;
        while (count++ < num){
            double[] balance = new double[loanNum];
            double[] balanceR = new double[loanNum];
            double[] balanceL = new double[loanNum];
            //获取随机相关系数矩阵
            Matrix randomM = MatrixUtil.getRandomCovMatrix(cov, loanNum,quarterNum);
            //统一同一贷款人在随机矩阵中的值
            long[] ids = assetPoolInfo.getIds();
            for(int i = 0; i< loanNum -1; i++){
                for(int c = i+1; c< loanNum; c++){
                    if(ids[i]==ids[c]){
                        for(int k = 0;k<randomM.getColumnCount();k++){
                            randomM.setAsDouble(randomM.getAsDouble(i,k),c,k);
                        }
                    }
                }
            }
            //判定每笔资产
            for(int i = 0; i< loanNum; i++){
                double p = principal[i];
                int k = 0;
                double ceil = maturity[i];//需要计算的到期期限数，若为年，则表示期限年数
                while (k<ceil){
                    if(randomM.getAsDouble(i,k) <= conInvM.getAsDouble(i,k)){
                        double last = record.getAsDouble(i, k);
                        record.setAsDouble(last+1,i,k);
                        balance[i] = Math.max(p-secureAmount[i],0);
                        balanceR[i] = balance[i] * rr[i];
                        balanceL[i] = balance[i] * (1-rr[i]);
                    }
                    p = p - amortisation.getAsDouble(i,k);
                    k++;
                }
            }

            double balanceSum = StatUtils.sum(balance);
            double balanceRSum = StatUtils.sum(balanceR);
            sumDefault += balanceSum;
            sumRecovery += balanceRSum;

            Integer defaultRateIndex = MathUtil.ceil(balanceSum / totalPrincipal * precision);
            defaultRate[defaultRateIndex] += 1;
            Integer recoveryRateIndex = MathUtil.ceil(balanceRSum / totalPrincipal * precision);
            if(recoveryRateIndex<0){
                System.out.println("recoveryRateIndex:"+recoveryRateIndex);
                recoveryRateIndex = 0;
            }
            recoveryRate[recoveryRateIndex] += 1 ;
            Integer lossRateIndex = MathUtil.ceil(Math.max(0,StatUtils.sum(balanceL)-reservesMoney)/totalPrincipal*precision);
            lossRate[lossRateIndex] += 1;

            alreadyNum.incrementAndGet();
        }

        //按季度计算违约比率
        if(sumDefault > 0){
            double[] outamor = new double[loanNum];
            for(int j=0;j<quarterNum;j++){
                double vertAmount = 0;
                for(int i = 0; i< loanNum; i++){
                    if(j>=1){
                        outamor[i] = outamor[i] + amortisation.getAsDouble(i,j-1);
                    }else {
                        outamor[i] = 0;
                    }
                    vertAmount = vertAmount + record.getAsDouble(i,j)*Math.max(principal[i]-outamor[i]-secureAmount[i],0);
                }
                defaultRateByPeriod[j] = vertAmount/sumDefault;
            }
        }
        MonteResult monteResult = new MonteResult();
        monteResult.setDefaultRate(defaultRate);
        monteResult.setRecoveryRate(recoveryRate);
        monteResult.setLossRate(lossRate);
        monteResult.setDefaultRateByPeriod(defaultRateByPeriod);
        return monteResult;
    }
}
