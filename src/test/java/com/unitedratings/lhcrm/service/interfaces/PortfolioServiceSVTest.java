package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.business.PortfolioDataCalculate;
import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.AssetPool;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.utils.CustomMultivariateNormalDistribution;
import com.unitedratings.lhcrm.utils.MathUtil;
import com.unitedratings.lhcrm.utils.MatrixUtil;
import org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ujmp.core.Matrix;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyongxin
 * @createAt 2017-10-24 下午4:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PortfolioServiceSVTest {

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Autowired
    private PortfolioDataCalculate calculate;

    @Test
    public void getPortfolioById() throws Exception {
        Portfolio portfolio = portfolioService.getPortfolioById(50l);
        Map<String, Long> collect = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.counting()));
        System.out.println(collect);
        Map<String, DoubleSummaryStatistics> collect1 = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.summarizingDouble(r -> r.getDebtorInfo().getLoanBalance())));
        Map<String, Double> collect2 = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.summingDouble(r -> r.getDebtorInfo().getLoanBalance())));
        System.out.println(collect1);
        System.out.println(collect2);
        portfolio.getRecordList().stream().collect(Collectors.groupingBy(record->record.getDebtorInfo().getBorrowerIndustry())).forEach((key,recordList)->{
            Map<Long, Long> collect3 = recordList.stream().collect(Collectors.groupingBy(r -> r.getDebtorInfo().getBorrowerSerial(), Collectors.counting()));
            System.out.println(collect3);
        });
    }

    @Test
    public void testRandomCovMatrix(){
        //16=13,24=31,25=60,35=7,36=31,49=6,51=17,57=15,58=10,71=314,72=9,73=12
        AssetPool assetPool = calculate.assembleAssetPool(36L, SummaryType.QUARTER.getValue());
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        Integer maxQuarter = MathUtil.getMaxQuarter(assetPoolInfo.getMaturity());
        long t1 = System.currentTimeMillis();
        Matrix chol = assetPoolInfo.getCorrelation().chol();
        for(int i=0;i<200000;i++){
            Matrix matrix1 = MatrixUtil.getRandomCovMatrix(chol, assetPoolInfo.getLoanNum(), maxQuarter);
        }
        long t2 = System.currentTimeMillis();
        /*AbstractMultivariateRealDistribution covSampleDistribution = new CustomMultivariateNormalDistribution(new double[assetPoolInfo.getLoanNum()], assetPoolInfo.getCorrelation().toDoubleArray());
        Matrix matrix2 = MatrixUtil.getRandomCovMatrixFromMulti(covSampleDistribution, assetPoolInfo.getLoanNum(), maxQuarter);
        long t3 = System.currentTimeMillis();*/
        System.out.println("getRandomCovMatrix耗时："+(t2-t1)+"ms");
        //System.out.println("getRandomCovMatrix1耗时："+(t3-t2)+"ms");
        //AssetsExcelProcess.outputMatrixToExcel(assetPoolInfo.getCorrelation(),chol,matrix1,matrix2);
    }

    @Test
    public void testRandomCovMulti(){
        AssetPool assetPool = calculate.assembleAssetPool(36L, SummaryType.QUARTER.getValue());
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        Integer maxQuarter = MathUtil.getMaxQuarter(assetPoolInfo.getMaturity());
        long t1 = System.currentTimeMillis();
        AbstractMultivariateRealDistribution covSampleDistribution = new CustomMultivariateNormalDistribution(new double[assetPoolInfo.getLoanNum()], assetPoolInfo.getCorrelation().toDoubleArray());
        for(int i=0;i<200000;i++){
            Matrix matrix2 = MatrixUtil.getRandomCovMatrixFromMulti(covSampleDistribution, assetPoolInfo.getLoanNum(), maxQuarter);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("getRandomCovMatrixFromMulti耗时："+(t2-t1)+"ms");
    }

}