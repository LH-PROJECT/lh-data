package com.unitedratings.lhcrm.business;

import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.*;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.service.interfaces.AmortizationServiceSV;
import com.unitedratings.lhcrm.service.interfaces.IdealServiceSV;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.service.interfaces.SysDictionaryServiceSV;
import com.unitedratings.lhcrm.utils.AssetAnalysisUtil;
import com.unitedratings.lhcrm.utils.DateUtil;
import com.unitedratings.lhcrm.utils.MathUtil;
import com.unitedratings.lhcrm.utils.SpringApplicationContextUtil;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.util.*;

/**
 * @author wangyongxin
 * @createAt 2017-10-31 下午5:37
 **/
@Component
public class PortfolioDataCalculate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioDataCalculate.class);

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Autowired
    private SysDictionaryServiceSV dictionaryService;

    @Autowired
    private AmortizationServiceSV amortizationService;

    @Autowired
    private IdealServiceSV idealService;

    /**
     * 封装资产池信息
     * @return
     */
    public AssetPool prepareAssetPoolInfo(Long portfolioId,Integer summaryType) throws BusinessException {
        AssetPool assetPool = null;
        try {
            //查询封装资产池信息
            assetPool = assembleAssetPool(portfolioId,summaryType);
            AssetPoolInfo info = assetPool.getAssetPoolInfo();
            //封装理想违约率
            assembleIdealDefaultMatrix(info);
            //基础计算数据处理
            dataPreparation(assetPool);
            //保存资产池信息
            saveAssetPool(assetPool,portfolioId);
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
    private AssetPool assembleAssetPool(Long portfolioId,Integer summaryType) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
        AssetPool assetPool = null;
        if(portfolio!=null){
            assetPool = new AssetPool();
            assetPool.setPortfolio(portfolio);
            assetPool.setLoanRecords(portfolio.getRecordList());
            AssetPoolInfo info = new AssetPoolInfo();
            info.setPortfolioName(portfolio.getPortfolioName());
            info.setLoanNum(portfolio.getRecordList().size());
            info.setSummaryType(summaryType);
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
     * 封装资产相关系数矩阵
     * @param portfolio
     */
    private Matrix assembleCorrelation(Portfolio portfolio) {
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
     * 封装分期摊还信息
     * @param portfolio
     * @param info
     */
    private void assembleAmortisation(Portfolio portfolio, AssetPoolInfo info) {
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
     * 查询数据库封装理想违约率矩阵
     * @param info
     */
    private void assembleIdealDefaultMatrix(AssetPoolInfo info) {
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

    /**
     * 通用计算数据准备
     * @param assetPool
     */
    private void dataPreparation(AssetPool assetPool) {
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
     * 保存资产池信息到数据库
     * @param assetPool
     */
    private void saveAssetPool(AssetPool assetPool,Long portfolioId) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        PortfolioServiceSV portfolioService = applicationContext.getBean(PortfolioServiceSV.class);
        Portfolio portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        portfolio.setIdealDefaultId(assetPool.getAssetPoolInfo().getIdealDefaultId());
        portfolio.setRecordList(assetPool.getLoanRecords());
        portfolioService.updatePortfolio(portfolio);
    }

    /**
     * 资产池统计量（该方法必须在债项评级之后调用）
     * @param assetPool
     */
    public void statisticalAssetPool(AssetPool assetPool) {
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
}
