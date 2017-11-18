package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.IndustryDao;
import com.unitedratings.lhcrm.entity.Industry;
import com.unitedratings.lhcrm.service.interfaces.IndustryServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangyongxin
 */
@Service
@Transactional
public class IndustryServiceSVImpl implements IndustryServiceSV {

    @Autowired
    private IndustryDao industryDao;

    @Override
    public void saveIndustryList(List<Industry> industries) {
        industryDao.save(industries);
    }

    @Override
    public List<Industry> getIndustryLikeCode(String value) {
        return industryDao.findByIndustryCodeStartingWith(value);
    }
}
