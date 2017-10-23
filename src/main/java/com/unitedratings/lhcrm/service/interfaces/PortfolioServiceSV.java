package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Portfolio;

/**
 * @author wangyongxin
 */
public interface PortfolioServiceSV {
    /**
     * 保存资产池信息
     * @param portfolio
     * @return
     */
    Portfolio savePortfolio(Portfolio portfolio);

    /**
     * 根据资产池id查询资产池信息
     * @param attachableId
     * @return
     */
    Portfolio getPortfolioById(Long attachableId);

    /**
     * 跟新资产池信息
     * @param portfolio
     */
    void updatePortfolio(Portfolio portfolio);
}
