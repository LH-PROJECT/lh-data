package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.GuarantorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface GuarantorInfoDao extends JpaRepository<GuarantorInfo,Long>{
    /**
     * 根据资产池id查询保证人信息
     * @param portfolioId
     * @return
     */
    List<GuarantorInfo> findByPortfolioId(Long portfolioId);
}
