package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.AmortizationInfo;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface AmortizationServiceSV {
    /**
     * 根据资产池id获取每笔资产分期摊还信息
     * @param id
     * @return
     */
    List<AmortizationInfo> getAmortizationInfoListByPortfolioId(Long id);
}
