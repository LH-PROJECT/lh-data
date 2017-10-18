package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.*;
import com.unitedratings.lhcrm.entity.DebtorInfo;
import com.unitedratings.lhcrm.entity.GuarantorInfo;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.entity.SysDictionary;
import com.unitedratings.lhcrm.service.interfaces.SysDictionaryServiceSV;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.util.List;
import java.util.Map;

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
                targetDefaultProbability[i] = perfectDefaultRate.getAsDouble(ceil-1,i);
            }else {
                double defaultRateLow = perfectDefaultRate.getAsDouble(floor-1, i);
                double defaultRateUp = perfectDefaultRate.getAsDouble(ceil-1, i);
                if(defaultRateLow == defaultRateUp){
                    targetDefaultProbability[i] = defaultRateUp;
                }else {
                    targetDefaultProbability[i] = defaultRateLow + (year-floor)*(defaultRateUp-defaultRateLow)/(ceil-floor);
                }
            }
            int position = Math.max((int) Math.floor(num * targetDefaultProbability[i]),1);
            targetDefaultRate[i] =  (double) MathUtil.getProbabilityIndex(result.getDefaultRate(),position)/(Constant.PRECISION-1);
            targetRecoveryRate[i] = (double) MathUtil.getProbabilityIndex(result.getRecoveryRate(),position)/(Constant.PRECISION-1);
            targetLossRate[i] = (double) MathUtil.getProbabilityIndex(result.getLossRate(),position)/(Constant.PRECISION-1);
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

    /**
     * 计算基础资产、担保人违约概率
     * @param assetPool
     * @param numRating
     * @param scRating
     * @param loanRating
     */
    public static void calAssetAndGuaranteeDefaultRate(AssetPool assetPool, int[] numRating, int[] scRating, int[] loanRating) {
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        double[] yearMaturity = assetPoolInfo.getYearMaturity();
        Matrix perfectDefaultRate = assetPoolInfo.getPerfectDefaultRate();
        double[] assetDR = new double[assetPoolInfo.getLoanNum()];
        double[] loanPD = new double[assetPoolInfo.getLoanNum()];
        double[] scDR = new double[assetPoolInfo.getLoanNum()];
        for(int i=0;i<assetPoolInfo.getLoanNum();i++){
            GuarantorInfo guarantorInfo = loanRecords.get(i).getGuarantorInfo();
            DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
            if(yearMaturity[i]<=1){
                assetDR[i] = yearMaturity[i]*perfectDefaultRate.getAsDouble(0,numRating[i]-1);
                loanPD[i] = yearMaturity[i]*perfectDefaultRate.getAsDouble(0,loanRating[i]-1);
                if(!"credit_loan".equals(guarantorInfo.getGuaranteeModeCode())){
                    scDR[i] = yearMaturity[i]*perfectDefaultRate.getAsDouble(0,scRating[i]-1);
                }
            } else {
                int floor = (int) Math.floor(yearMaturity[i]);
                int ceil = (int) Math.ceil(yearMaturity[i]);
                double low = perfectDefaultRate.getAsDouble(floor-1,numRating[i]-1);
                double up = perfectDefaultRate.getAsDouble(ceil-1,numRating[i]-1);
                if(low==up){
                    assetDR[i] = low;
                }else {
                    assetDR[i] = low + (yearMaturity[i]-floor)*(up-low)/(ceil-floor);
                }
                loanPD[i] = assetDR[i];

                if(!"credit_loan".equals(guarantorInfo.getGuaranteeModeCode())){
                    //担保人级别
                    double scLow = perfectDefaultRate.getAsDouble(floor-1, scRating[i]-1);
                    double scUp = perfectDefaultRate.getAsDouble(ceil-1, scRating[i]-1);
                    if(scLow==scUp){
                        scDR[i] = scLow;
                    }else {
                        scDR[i] = scLow + (yearMaturity[i]-floor)*(scUp-scLow)/(ceil-floor);
                    }
                    //债项级别
                    double loanLow = perfectDefaultRate.getAsDouble(floor-1, loanRating[i]-1);
                    double loanUp = perfectDefaultRate.getAsDouble(ceil-1, loanRating[i]-1);
                    if(loanLow==loanUp){
                        loanPD[i] = loanLow;
                    }else {
                        loanPD[i] = loanLow + (yearMaturity[i]-floor)*(loanUp-loanLow)/(ceil-floor);
                    }
                }
            }
            Double defaultMagnification = debtorInfo.getDefaultMagnification();
            if(defaultMagnification !=null){
                assetDR[i] = Math.min(defaultMagnification*assetDR[i],0.99999);
            }
            //累计违约率
            debtorInfo.setTotalDefaultRate(Math.min(assetDR[i],0.99999));
        }
        assetPoolInfo.setAssetDR(assetDR);
        assetPoolInfo.setScDR(scDR);
        assetPoolInfo.setLoanPD(loanPD);
    }

    /**
     * 计算条件违约概率
     * @param assetPool
     * @param numRating
     */
    public static void calConditionDefaultRate(AssetPool assetPool, int[] numRating) {
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        double[] yearMaturity = assetPoolInfo.getYearMaturity();
        Matrix perfectDefaultRate = assetPoolInfo.getPerfectDefaultRate();
        Matrix yearConMatrix = getConMatrix(numRating, loanRecords, assetPoolInfo, yearMaturity,perfectDefaultRate,SummaryType.YEAR.getValue());
        yearConMatrix.setLabel("按年条件违约率");
        if(SummaryType.QUARTER.getValue().equals(assetPoolInfo.getSummaryType())){
            Matrix quarterPerfectDefaultRate = new DefaultDenseDoubleMatrix2D(40,(int)perfectDefaultRate.getColumnCount());
            quarterPerfectDefaultRate.setLabel("按季度季度理想违约率");
            for(int i=0;i<perfectDefaultRate.getColumnCount();i++){
                for(int j=1;j<=40;j++){
                    int ceil = (int) Math.ceil((double) j / 4);
                    double rate = 0;
                    if(ceil<=1){
                        rate = perfectDefaultRate.getAsDouble(0, i) / 4 * j;
                    }else {
                        rate = perfectDefaultRate.getAsDouble(ceil - 2, i) + (perfectDefaultRate.getAsDouble(ceil-1, i) - perfectDefaultRate.getAsDouble(ceil - 2, i)) / 4 * (j - 4 * (ceil - 1));
                    }
                    quarterPerfectDefaultRate.setAsDouble(rate,j-1,i);
                }
            }
            Matrix quarterConMatrix = getConMatrix(numRating, loanRecords, assetPoolInfo, assetPoolInfo.getMaturity(), quarterPerfectDefaultRate,assetPoolInfo.getSummaryType());
            quarterConMatrix.setLabel("按季度条件违约率");
            assetPoolInfo.setConditionMatrix(quarterConMatrix);
        }else {
            assetPoolInfo.setConditionMatrix(yearConMatrix);
        }
    }

    /**
     * 计算阶段条件违约率
     * @param numRating
     * @param loanRecords
     * @param assetPoolInfo
     * @param maturity
     * @param perfectDefaultRate
     * @param summaryType
     * @return
     */
    private static Matrix getConMatrix(int[] numRating, List<LoanRecord> loanRecords, AssetPoolInfo assetPoolInfo, double[] maturity, Matrix perfectDefaultRate, Integer summaryType) {
        Matrix conMatrix = new DefaultDenseDoubleMatrix2D(assetPoolInfo.getLoanNum(), MathUtil.getMaxQuarter(maturity));
        for(int i=0;i<loanRecords.size();i++){
            DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
            Double defaultMagnification = debtorInfo.getDefaultMagnification();
            //处理第一年/季度
            StringBuilder defaultRateBySection = new StringBuilder();
            if(defaultMagnification!=null){
                double rate = perfectDefaultRate.getAsDouble(0, numRating[i]-1) * defaultMagnification;
                conMatrix.setAsDouble(rate,i,0);
                if(StringUtils.isEmpty(debtorInfo.getDefaultRateBySection())){
                    defaultRateBySection.append(rate);
                }
            }
            if(maturity[i]<=1){
                double defaultRate = conMatrix.getAsDouble(i,0) * maturity[i];
                if(SummaryType.QUARTER.getValue().equals(summaryType)){
                    defaultRate = defaultRate * 4;
                }
                conMatrix.setAsDouble(defaultRate,i,0);
                debtorInfo.setDefaultRateBySection(String.valueOf(defaultRate));
            }else {
                //处理其他年
                int ceil = (int) Math.ceil(maturity[i]);
                for(int j=1;j<ceil;j++){
                    double up = perfectDefaultRate.getAsDouble(j, numRating[i]-1);
                    double low = perfectDefaultRate.getAsDouble(j-1, numRating[i]-1);
                    //处理最后一年/季度
                    if(SummaryType.YEAR.getValue().equals(summaryType)&&j==ceil-1){
                        up = low + (up-low)*(maturity[i]-j);
                    }

                    if(defaultMagnification !=null){
                        up = up * defaultMagnification;
                        low = low * defaultMagnification;
                    }
                    double min = Math.min((up - low) / (1 - low), 0.99999);
                    if(min<0){
                        min = 0.99999;
                    }
                    conMatrix.setAsDouble(min,i,j);
                    defaultRateBySection.append(",").append(min);
                }
                if(StringUtils.isEmpty(debtorInfo.getDefaultRateBySection())){
                    debtorInfo.setDefaultRateBySection(defaultRateBySection.toString());
                }
            }
        }
        return conMatrix;
    }

    /**
     * 调整回收率(可计算出最终回收率)
     * @param assetPool
     * @param numRating
     */
    public static void adjustDefaultRate(AssetPool assetPool, int[] numRating) {
        List<LoanRecord> loanRecords = assetPool.getLoanRecords();
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        double[] assetDR = assetPoolInfo.getAssetDR();
        double[] scDR = assetPoolInfo.getScDR();
        double[] recoveryRate = new double[assetPoolInfo.getLoanNum()];
        double[] finalRecoveryRate = new double[assetPoolInfo.getLoanNum()];
        double[] covAs = new double[assetPoolInfo.getLoanNum()];
        //贷款人自身加权平均回收率
        double wars = 0;
        NormalDistribution normal = new NormalDistribution();
        for(int i = 0; i< loanRecords.size(); i++){
            DebtorInfo debtorInfo = loanRecords.get(i).getDebtorInfo();
            GuarantorInfo guarantorInfo = loanRecords.get(i).getGuarantorInfo();
            switch ((numRating[i]-1)/3){
                case 0:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.7;
                    break;
                case 1:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.75;
                    break;
                case 2:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.8;
                    break;
                case 3:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.85;
                    break;
                case 4:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.9;
                    break;
                case 5:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate()*0.95;
                    break;
                default:
                    recoveryRate[i] = debtorInfo.getAssetSelfRecoveryRate();
                    break;
            }
            finalRecoveryRate[i] = recoveryRate[i];
            wars += finalRecoveryRate[i]*debtorInfo.getLoanBalance();
            if(debtorInfo.getRelevanceForGuaranteeAndLender()!=null){
                covAs[i] = debtorInfo.getRelevanceForGuaranteeAndLender();
            }

            if(!"credit_loan".equals(guarantorInfo.getGuaranteeModeCode())){//非信用贷款
                if(!StringUtils.isEmpty(guarantorInfo.getGuaranteeIndustryCode())){//担保机构行业编码不为空,对应的担保比率必须不为空
                    if(scDR[i]<=assetDR[i]){//当前担保人违约率小于当前资产违约率
                        double z1 = normal.inverseCumulativeProbability(assetDR[i]);
                        double z2 = normal.inverseCumulativeProbability(scDR[i]);
                        double y1 = MathUtil.binNormalDistributionCumulativeFunction(z1,z2,covAs[i]);
                        double y2 = assetDR[i] - y1;
                        finalRecoveryRate[i] = 1-(1-recoveryRate[i])*(y1+y2*(1-guarantorInfo.getGuaranteeRatio()))/assetDR[i];
                    }
                }
            }
            debtorInfo.setGuaranteeRecoveryRate(finalRecoveryRate[i]);
            //增加抵押物回收率
            if(debtorInfo.getMortgageRecoveryRate()!=null){
                finalRecoveryRate[i] = Math.min(finalRecoveryRate[i] + debtorInfo.getMortgageRecoveryRate(),1);
            }
            //设置最终回收率
            debtorInfo.setFinalRecoveryRate(finalRecoveryRate[i]);
        }
        assetPoolInfo.setFinalRecoveryRate(finalRecoveryRate);
    }

    /**
     * 计算资产相关系数矩阵
     * @param portfolio
     * @param assetCorrelationCoefficient
     * @return
     */
    public static Matrix calCorrelationMatrix(Portfolio portfolio, Map<String, Double> assetCorrelationCoefficient) {
        int loanNum = portfolio.getRecordList().size();
        Matrix matrix = new DefaultDenseDoubleMatrix2D(loanNum,loanNum);
        matrix.setLabel("资产相关系数矩阵");
        for(int i=0;i<loanNum;i++){
            DebtorInfo debtorInfo_i = portfolio.getRecordList().get(i).getDebtorInfo();
            for (int j=0;j<loanNum;j++){
                DebtorInfo debtorInfo_j = portfolio.getRecordList().get(j).getDebtorInfo();
                double coefficient = 0;
                if(debtorInfo_i.getIndustryCode().equals(debtorInfo_j.getIndustryCode())){
                    if(debtorInfo_i.getBorrowerArea().equals(debtorInfo_j.getBorrowerArea())){
                        coefficient = assetCorrelationCoefficient.get("sameArea_sameIndustry")*portfolio.getMultiplier();
                    }else {
                        coefficient = assetCorrelationCoefficient.get("diffArea_sameIndustry")*portfolio.getMultiplier();
                    }
                }else {
                    if(debtorInfo_i.getBorrowerArea().equals(debtorInfo_j.getBorrowerArea())){
                        coefficient = assetCorrelationCoefficient.get("sameArea_diffIndustry")*portfolio.getMultiplier();
                    }else {
                        coefficient = assetCorrelationCoefficient.get("diffArea_diffIndustry");
                    }
                }
                if(i==j){
                    coefficient = 1;
                }
                matrix.setAsDouble(coefficient,i,j);
            }
        }
        return matrix;
    }

    /**
     * 计算违约放大倍数
     * @param assetPool
     * @param numRating
     * @param scRating
     * @param loanRating
     */
    public static void calDefaultMagnification(AssetPool assetPool, int[] numRating, int[] scRating, int[] loanRating) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        SysDictionaryServiceSV dictionaryService = applicationContext.getBean(SysDictionaryServiceSV.class);
        Portfolio portfolio = assetPool.getPortfolio();
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        List<LoanRecord> recordList = portfolio.getRecordList();
        if(!CollectionUtils.isEmpty(recordList)){
            SysDictionary dictionary = dictionaryService.getDictionaryById(portfolio.getSponsorId());
            if(dictionary!=null){
                portfolio.setSponsorName(dictionary.getParamDesc());
                double serviceM = 0;
                switch (dictionary.getParamValue()){
                    case "1":
                        serviceM = 0.6;
                        break;
                    case "2":
                        serviceM = 0.7;
                        break;
                    case "3":
                        serviceM = 0.8;
                        break;
                    case "4":
                        serviceM = 0.9;
                        break;
                    case "5":
                        serviceM = 1.0;
                        break;
                    default:
                        break;
                }
                if(serviceM>0){
                    double pSum = StatUtils.sum(assetPoolInfo.getPrincipal());
                    String[] area = new String[recordList.size()];
                    Long[] industry = new Long[recordList.size()];
                    Long[] borrower = new Long[recordList.size()];
                    for(int i = 0;i<recordList.size();i++) {
                        int level = (numRating[i] - 1) / 3;
                        double defaultMagnification = serviceM;
                        if (level >= 5) {
                            defaultMagnification = 1 + 0.5 * defaultMagnification;
                        } else {
                            defaultMagnification = 1 + 0.1 * level * defaultMagnification;
                        }
                        DebtorInfo debtorInfo = recordList.get(i).getDebtorInfo();
                        debtorInfo.setDefaultMagnification(defaultMagnification);
                        area[i] = debtorInfo.getBorrowerArea();
                        industry[i] = debtorInfo.getIndustryCode();
                        borrower[i] = debtorInfo.getBorrowerSerial();
                    }
                    for(int i = 0;i<recordList.size();i++){
                        DebtorInfo debtorInfo_i = recordList.get(i).getDebtorInfo();
                        //对地区集中度进行放大
                        double areaSum = debtorInfo_i.getLoanBalance();
                        for(int j=i+1;j<recordList.size();j++){
                            DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                            if(debtorInfo_j.getBorrowerArea().equals(area[i])&&area[i]!=null){
                                areaSum += debtorInfo_j.getLoanBalance();
                                area[i] = null;
                            }
                        }
                        double mtp_area = 0;
                        double rat = areaSum / pSum;
                        if(rat>=0.15){
                            if(rat>=0.3){
                                mtp_area  = 1+0.5*serviceM;
                            }else if(rat<0.3&&rat>=0.2){
                                mtp_area = 1+0.25*serviceM+2.5*serviceM*(rat-0.2);
                            }else {
                                mtp_area = 1+0.1*serviceM+3*serviceM*(rat-0.15);
                            }
                            for(int j = i;j<recordList.size();j++){
                                DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                                if(debtorInfo_j.getBorrowerArea().equals(debtorInfo_i.getBorrowerArea())){
                                    debtorInfo_j.setDefaultMagnification(Math.max(mtp_area,debtorInfo_j.getDefaultMagnification()));
                                }
                            }
                        }
                        //对行业集中度进行放大
                        double industrySum = debtorInfo_i.getLoanBalance();
                        for(int j=i+1;j<recordList.size();j++){
                            DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                            if(debtorInfo_j.getIndustryCode().equals(industry[i])&&industry[i]!=null){
                                industrySum += debtorInfo_j.getLoanBalance();
                                industry[i] = null;
                            }
                        }
                        double mtp_ind = 0;
                        rat = industrySum/pSum;
                        if(rat>=0.15){
                            if(rat>=0.3){
                                mtp_ind  = 1+0.5*serviceM;
                            }else if(rat<0.3&&rat>=0.2){
                                mtp_ind = 1+0.25*serviceM+2.5*serviceM*(rat-0.2);
                            }else {
                                mtp_ind = 1+0.1*serviceM+3*serviceM*(rat-0.15);
                            }
                            for(int j = i;j<recordList.size();j++){
                                DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                                if(debtorInfo_j.getIndustryCode().equals(debtorInfo_i.getIndustryCode())){
                                    debtorInfo_j.setDefaultMagnification(Math.max(mtp_ind,debtorInfo_j.getDefaultMagnification()));
                                }
                            }
                        }
                        //对前5大借款人进行放大
                        double obligorSum = debtorInfo_i.getLoanBalance();
                        for(int j=i+1;j<recordList.size();j++){
                            DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                            if(debtorInfo_j.getBorrowerSerial().equals(borrower[i])&&borrower[i]!=null){
                                obligorSum += debtorInfo_j.getLoanBalance();
                                borrower[i] = null;
                            }
                        }
                        rat = obligorSum/pSum;
                        double mtp_obg = 0;
                        if(rat>=0.05){
                            if(rat>=0.15){
                                mtp_obg  = 1+1.5*serviceM;
                            }else if(rat<0.15&&rat>=0.1){
                                mtp_obg = 1+serviceM+10*serviceM*(rat-0.1);
                            }else {
                                mtp_obg = 1+0.5*serviceM+10*serviceM*(rat-0.05);
                            }
                            for(int j = i;j<recordList.size();j++){
                                DebtorInfo debtorInfo_j = recordList.get(j).getDebtorInfo();
                                if(debtorInfo_j.getBorrowerSerial().equals(debtorInfo_i.getBorrowerSerial())){
                                    debtorInfo_j.setDefaultMagnification(Math.max(mtp_obg,debtorInfo_j.getDefaultMagnification()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
