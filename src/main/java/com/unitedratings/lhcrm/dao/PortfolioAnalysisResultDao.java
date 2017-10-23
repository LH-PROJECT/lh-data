package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface PortfolioAnalysisResultDao extends JpaRepository<PortfolioAnalysisResult,Long> {
    /**
     * 按时间顺序降序排列资产池模拟结果
     * @param id
     * @return
     */
    List<PortfolioAnalysisResult> findByPortfolioIdOrderByCreateTimeDesc(Long id);
}
