package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;

public interface PortfolioAnalysisServiceSV {
    PortfolioAnalysisResult saveAnalysisResult(PortfolioAnalysisResult analysisResult);

    PortfolioAnalysisResult findLastAnalysisResultByPortfolioId(Long id);
}
