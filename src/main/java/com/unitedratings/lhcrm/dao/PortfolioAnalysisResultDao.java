package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioAnalysisResultDao extends JpaRepository<PortfolioAnalysisResult,Long> {
    List<PortfolioAnalysisResult> findByPortfolioIdOrderByCreateTimeDesc(Long id);
}
