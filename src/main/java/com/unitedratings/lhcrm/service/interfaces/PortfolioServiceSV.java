package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.web.model.PageModel;
import com.unitedratings.lhcrm.web.model.PortfolioQuery;
import org.springframework.data.domain.Page;

import java.util.List;

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

    /**
     * 获取资产池列表
     * @param query
     * @return
     */
    Page<Portfolio> getPortfolioList(PageModel<PortfolioQuery> query);

    /**
     * 根据资产池id删除资产池
     * @param id
     */
    boolean deletePortfolioById(Long id);
}
