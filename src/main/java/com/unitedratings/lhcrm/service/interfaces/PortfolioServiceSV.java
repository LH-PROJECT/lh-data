package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Portfolio;

public interface PortfolioServiceSV {
    Portfolio savePortfolio(Portfolio portfolio);

    Portfolio getPortfolioById(Long attachableId);

    void updatePortfolio(Portfolio portfolio);
}
