package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.DebtorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtorInfoDao extends JpaRepository<DebtorInfo,Long>{

    List<DebtorInfo> findByPortfolioId(Long portfolioId);
}
