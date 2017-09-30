package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.AmortizationInfo;

import java.util.List;

public interface AmortizationServiceSV {
    List<AmortizationInfo> getAmortizationInfoListByPortfolioId(Long id);
}
