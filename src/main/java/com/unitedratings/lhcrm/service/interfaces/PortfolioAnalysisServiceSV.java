package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;

/**
 * @author wangyongxin
 */
public interface PortfolioAnalysisServiceSV {
    /**
     * 保存资产池模拟结果
     * @param analysisResult
     * @return
     */
    PortfolioAnalysisResult saveAnalysisResult(PortfolioAnalysisResult analysisResult);

    /**
     * 根据资产池id获取最新一条的模拟结果
     * @param id
     * @return
     */
    PortfolioAnalysisResult findLastAnalysisResultByPortfolioId(Long id);

    /**
     * 根据id获取分析结果
     * @param id
     * @return
     */
    PortfolioAnalysisResult findAnalysisResultById(Long id);
}
