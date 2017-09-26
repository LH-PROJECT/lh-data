package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.AmortizationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmortizationInfoDao extends JpaRepository<AmortizationInfo,Long>{
    List<AmortizationInfo> findByPortfolioId(Long portfolioId);
}
