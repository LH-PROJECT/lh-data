package com.unitedratings.lhcrm.interceptors;

import com.unitedratings.lhcrm.domains.FinalMonteResult;
import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;

@Component
public class AnalysisResultInterceptor extends TimeoutDeferredResultProcessingInterceptor {

    @Autowired
    private PortfolioAnalysisServiceSV analysisServiceSV;


    @Override
    public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {
        if(concurrentResult instanceof PortfolioStatisticalResult){
            PortfolioStatisticalResult statisticalResult = (PortfolioStatisticalResult) concurrentResult;
            FinalMonteResult finalMonteResult = new FinalMonteResult();
            BeanUtils.copyProperties(statisticalResult.getMonteResult(), finalMonteResult,"defaultRecordMatrix");
            statisticalResult.setMonteResult(finalMonteResult);
        }
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        Object result = deferredResult.getResult();
        if(result instanceof Exception){
            Exception ex = (Exception) result;
            throw ex;
        }
    }
}
