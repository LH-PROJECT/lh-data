package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.AmortizationInfo;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.web.model.LoanRecordVo;
import com.unitedratings.lhcrm.web.model.PageModel;
import com.unitedratings.lhcrm.web.model.PageResult;
import com.unitedratings.lhcrm.web.model.PortfolioQuery;
import org.springframework.data.domain.Page;

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
     * 更新资产池信息
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

    /**
     * 根据资产池模拟记录分页查询资产池信息
     * @param query
     * @return
     */
    PageResult<Portfolio> getPortfolioListOnSimulationRecord(PageModel<PortfolioQuery> query);

    /**
     * 获取完整的资产池信息（包括贷款记录，分期摊还信息）
     * @param id
     * @return
     */
    Portfolio getFullPortfolioInfoById(Long id);

    /**
     * 更新资产池贷款记录信息
     * @param recordVo
     */
    boolean updateLoanRecord(LoanRecordVo recordVo) throws BusinessException;

    /**
     * 更新资产池贷款记录分期摊还信息
     * @param info
     */
    boolean updateAmortization(AmortizationInfo info);
}
