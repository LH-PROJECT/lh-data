package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.DebtorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface DebtorInfoDao extends JpaRepository<DebtorInfo,Long>{

    /**
     * 根据资产池id查询借款人信息
     * @param portfolioId
     * @return
     */
    List<DebtorInfo> findByPortfolioId(Long portfolioId);
}
