package com.unitedratings.lhcrm.interceptors;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;

import java.util.Date;

@Component
public class AnalysisResultInterceptor extends TimeoutDeferredResultProcessingInterceptor {

    @Autowired
    private PortfolioAnalysisServiceSV analysisServiceSV;

    @Override
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        Object result = deferredResult.getResult();
        if(result instanceof PortfolioStatisticalResult){
            PortfolioStatisticalResult statisticalResult = (PortfolioStatisticalResult) result;
            PortfolioAnalysisResult portfolioAnalysisResult = new PortfolioAnalysisResult();
            portfolioAnalysisResult.setResultFilePath(statisticalResult.getResultFilePath());
            portfolioAnalysisResult.setUploadRecordId(statisticalResult.getUploadRecordId());
            portfolioAnalysisResult.setStandardDeviation(statisticalResult.getStandardDeviation());
            portfolioAnalysisResult.setCreateTime(new Date());
            portfolioAnalysisResult.setAverageDefaultRate(statisticalResult.getAverageDefaultRate());
            portfolioAnalysisResult.setPortfolioDefaultDistribution(JSON.toJSONString(statisticalResult.getPortfolioDefaultDistribution()));
            portfolioAnalysisResult.setMonteResult(JSON.toJSONString(statisticalResult.getMonteResult()));
            portfolioAnalysisResult.setMonteSummaryResult(JSON.toJSONString(statisticalResult.getMonteSummaryResult()));
            PortfolioAnalysisResult analysisResult = analysisServiceSV.saveAnalysisResult(portfolioAnalysisResult);
            statisticalResult.setCreateTime(analysisResult.getCreateTime());
            statisticalResult.setId(analysisResult.getId());
            deferredResult.setResult((T)statisticalResult);
        }
    }


}
