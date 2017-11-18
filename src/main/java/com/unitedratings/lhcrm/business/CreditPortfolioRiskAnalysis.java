package com.unitedratings.lhcrm.business;

import com.unitedratings.lhcrm.algorithm.MonteCarlo;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.MonteResult;
import com.unitedratings.lhcrm.utils.MatrixUtil;
import org.ujmp.core.Matrix;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangyongxin
 */
public class CreditPortfolioRiskAnalysis implements Callable<MonteResult>{

    private Matrix cov;
    private Matrix conInvM;
    private AtomicInteger alreadyNum;
    private Integer needNum;
    private AssetPoolInfo assetPoolInfo;
    private Integer precision;

    public CreditPortfolioRiskAnalysis(AssetPoolInfo info,Integer num,AtomicInteger completedNum,final Integer precision,Matrix conInvM,Matrix cov){
        this.needNum = num;
        this.assetPoolInfo = info;
        this.alreadyNum = completedNum;
        this.precision = precision;
        this.conInvM = conInvM;
        this.cov = cov;
    }

    @Override
    public MonteResult call() throws Exception {
        return MonteCarlo.AmortizedM(assetPoolInfo,cov,conInvM,assetPoolInfo.getFinalRecoveryRate(),needNum,alreadyNum,precision);
    }

}
