package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.PortfolioAnalysisResultDao;
import com.unitedratings.lhcrm.dao.PortfolioDao;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangyongxin
 */
@Service
@Transactional
public class PortfolioAnalysisServiceSVImpl implements PortfolioAnalysisServiceSV {

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private PortfolioAnalysisResultDao portfolioAnalysisResultDao;

    @Override
    public PortfolioAnalysisResult saveAnalysisResult(PortfolioAnalysisResult analysisResult) {
        Portfolio portfolio = portfolioDao.findOne(analysisResult.getPortfolioId());
        if(portfolio!=null){
            portfolioDao.updateSimulationNum(portfolio.getId(),portfolio.getSimulationNum()+1);
            analysisResult = portfolioAnalysisResultDao.save(analysisResult);
        }
        return analysisResult;
    }

    @Override
    public PortfolioAnalysisResult findLastAnalysisResultByPortfolioId(Long id) {
        List<PortfolioAnalysisResult> analysisResults = portfolioAnalysisResultDao.findByPortfolioIdOrderByCreateTimeDesc(id);
        if(!CollectionUtils.isEmpty(analysisResults)){
            return analysisResults.get(0);
        }
        return null;
    }
}
