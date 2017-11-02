package com.unitedratings.lhcrm.business;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.domains.LargeAmountLoan;
import com.unitedratings.lhcrm.domains.LargeAmountResult;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.DebtorInfo;
import com.unitedratings.lhcrm.entity.LargeAmountSettings;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.entity.SysDictionary;
import com.unitedratings.lhcrm.service.interfaces.LargeAmountSettingsService;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.service.interfaces.SysDictionaryServiceSV;
import com.unitedratings.lhcrm.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangyongxin
 * @createAt 2017-10-31 上午10:42
 **/
@Component
public class LargeAmountTestCalculate {

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Autowired
    private SysDictionaryServiceSV dictionaryService;

    @Autowired
    private LargeAmountSettingsService largeAmountSettingsService;

    private List<SysDictionary> creditLevelList;
    private List<SysDictionary> guaranteeModeList;
    private DenseDoubleMatrix2D matrix;

    @PostConstruct
    public void init(){
        SysDictionary creditLevel = dictionaryService.getDictByCodeAndVersion("CREDIT_LEVEL", 1.0);
        SysDictionary guaranteeMode = dictionaryService.getDictByCodeAndVersion("GUARANTEE_MODE", 1.0);
        if(creditLevel!=null){
            creditLevelList = dictionaryService.getDictionaryListByParentId(creditLevel.getId());
        }
        if(guaranteeMode!=null){
            guaranteeModeList = dictionaryService.getDictionaryListByParentId(guaranteeMode.getId());
        }
        LargeAmountSettings newestSettings = largeAmountSettingsService.getNewestSettings();
        if(newestSettings!=null){
            List<Double> doubles = JSON.parseArray(newestSettings.getSettingsDetail(), Double.class);
            if(!CollectionUtils.isEmpty(doubles)){
                int size = (int) Math.sqrt(doubles.size());
                matrix = new DefaultDenseDoubleMatrix2D(size,size);
                int index = 0;
                for(long[] co:matrix.allCoordinates()){
                    matrix.setAsDouble(doubles.get(index++),co);
                }
                matrix.setLabel("settings");
            }
        }
    }

    /**
     * 计算资产池对应信用级别最低分层支持要求
     * @param portfolioId
     * @param creditLevel
     * @return
     */
    public LargeAmountResult calculate(Long portfolioId, String creditLevel) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
        LargeAmountResult result = null;
        if(portfolio!=null){
            List<LoanRecord> recordList = portfolio.getRecordList();
            List<LargeAmountLoan> amountLoans = new ArrayList<>();
            recordList.stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerSerial())).forEach((key,list)->{
                LargeAmountLoan largeAmountLoan = new LargeAmountLoan();
                largeAmountLoan.setObligorSerial(key.intValue());
                double amount = 0;
                double rate = 0;
                for(LoanRecord record:list){
                    Double loanBalance = record.getDebtorInfo().getLoanBalance();
                    amount += loanBalance;
                    rate += loanBalance * record.getDebtorInfo().getMortgageRecoveryRate();
                }
                largeAmountLoan.setLoanAmount(amount);
                DebtorInfo debtorInfo = list.get(0).getDebtorInfo();
                largeAmountLoan.setDebtorCreditLevel(debtorInfo.getDebtLevel());
                largeAmountLoan.setDebtorCreditLevelCode(getNumRatingCreditCode(debtorInfo.getDebtLevel()));
                largeAmountLoan.setCollateralRecoveryRate(rate/amount);
                /*String mortgageLoan = "抵押贷款";
                if(mortgageLoan.equals(list.get(0).getGuarantorInfo().getGuaranteeMode())){
                    largeAmountLoan.setCollateralRecoveryRate(debtorInfo.getMortgageRecoveryRate());
                }else {
                    largeAmountLoan.setCollateralRecoveryRate(0.0);
                }*/
                amountLoans.add(largeAmountLoan);
            });
            Integer creditLevelCode = getNumRatingCreditCode(creditLevel);
            if(!CollectionUtils.isEmpty(amountLoans)){
                result = new LargeAmountResult();
                List<SysDictionary> subList = creditLevelList.subList(1, 19);
                double totalLoanAmount = 0;
                for(LargeAmountLoan loan:amountLoans){
                    Integer debtorCreditLevelCode = loan.getDebtorCreditLevelCode();
                    totalLoanAmount += loan.getLoanAmount();
                    List<Double> amounts =  new ArrayList<>(subList.size());
                    for(SysDictionary dict:subList){
                        double amount = 0;
                        int level = Integer.parseInt(dict.getParamValue());
                        if(debtorCreditLevelCode.compareTo(level) >= 0 && debtorCreditLevelCode.compareTo(creditLevelCode)>0){
                            amount = loan.getLoanAmount()*(1-loan.getCollateralRecoveryRate());
                        }else {
                            amount = 0;
                        }
                        amounts.add(amount);
                    }
                    loan.setAmountList(amounts);
                }
                List<Integer> levelOffset = new ArrayList<>(subList.size());
                List<Double> grossAmountList = new ArrayList<>(subList.size());
                List<Double> grossAmountWith5PercentList = new ArrayList<>(subList.size());
                for(int i = 0;i<subList.size();i++){
                    final SysDictionary dict = subList.get(i);
                    int level = Integer.parseInt(dict.getParamValue());
                    final int offset = (int) matrix.getAsDouble(level - 1, creditLevelCode - 1);
                    levelOffset.add(offset);
                    List<Double> orderedAmountList = new ArrayList<>();
                    for(LargeAmountLoan loan:amountLoans){
                        orderedAmountList.add(loan.getAmountList().get(i));
                    }
                    System.out.println("排序前："+orderedAmountList);
                    orderedAmountList.sort(Comparator.reverseOrder());
                    double grossAmount = 0;
                    int count = 0;
                    while (count<offset){
                        grossAmount += orderedAmountList.get(count);
                        count++;
                    }
                    grossAmountList.add(grossAmount);
                    grossAmountWith5PercentList.add(grossAmount*0.95);
                }
                result.setLevelDifference(levelOffset);
                result.setGrossAmountList(grossAmountList);
                result.setGrossAmountListAfter5PercentRecovery(grossAmountWith5PercentList);
                result.setLargeAmountLoanList(amountLoans);
                result.setTargetCreditLevel(creditLevel);
                result.setTargetCreditLevelCode(creditLevelCode);
                result.setMinimumSupport(MathUtil.max(grossAmountWith5PercentList)/totalLoanAmount);
            }
        }
        return result;
    }

    /**
     * 获取信用等级对应量化值
     * @param creditLevel
     * @return
     */
    private Integer getNumRatingCreditCode(String creditLevel) {
        for(SysDictionary credit:creditLevelList){
            if(creditLevel.equals(credit.getParamCode())){
                return Integer.parseInt(credit.getParamValue());
            }
        }
        return null;
    }
}
