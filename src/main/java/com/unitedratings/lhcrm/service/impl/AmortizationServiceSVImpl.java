package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.AmortizationDao;
import com.unitedratings.lhcrm.dao.AmortizationInfoDao;
import com.unitedratings.lhcrm.entity.AmortizationInfo;
import com.unitedratings.lhcrm.service.interfaces.AmortizationServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AmortizationServiceSVImpl implements AmortizationServiceSV {

    @Autowired
    private AmortizationDao amortizationDao;

    @Autowired
    private AmortizationInfoDao amortizationInfoDao;

    @Override
    public List<AmortizationInfo> getAmortizationInfoListByPortfolioId(Long id) {
        return amortizationInfoDao.findByPortfolioId(id);
    }
}
