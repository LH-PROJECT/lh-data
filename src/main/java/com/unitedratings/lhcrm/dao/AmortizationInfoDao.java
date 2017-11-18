package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.AmortizationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface AmortizationInfoDao extends JpaRepository<AmortizationInfo,Long>{
    /**
     * 根据资产池id查询分期摊还信息
     * @param portfolioId
     * @return
     */
    List<AmortizationInfo> findByPortfolioId(Long portfolioId);
}
