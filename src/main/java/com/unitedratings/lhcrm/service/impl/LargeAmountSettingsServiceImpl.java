package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.LargeAmountSettingsDao;
import com.unitedratings.lhcrm.entity.LargeAmountSettings;
import com.unitedratings.lhcrm.service.interfaces.LargeAmountSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-10-30 下午3:37
 **/
@Service
public class LargeAmountSettingsServiceImpl implements LargeAmountSettingsService {

    @Autowired
    private LargeAmountSettingsDao amountSettingsDao;

    @Override
    @Transactional
    public LargeAmountSettings saveSettings(LargeAmountSettings largeAmountSettings) {
        return amountSettingsDao.save(largeAmountSettings);
    }

    @Override
    public LargeAmountSettings getNewestSettings() {
        List<LargeAmountSettings> settings = amountSettingsDao.findAll(new Sort(Sort.Direction.DESC, "createTime"));
        if(!CollectionUtils.isEmpty(settings)){
            return settings.get(0);
        }
        return null;
    }
}
