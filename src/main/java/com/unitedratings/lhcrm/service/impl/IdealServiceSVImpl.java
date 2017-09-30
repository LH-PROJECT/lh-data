package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.IdealDefaultDao;
import com.unitedratings.lhcrm.dao.IdealDefaultItemDao;
import com.unitedratings.lhcrm.entity.IdealDefault;
import com.unitedratings.lhcrm.entity.IdealDefaultItem;
import com.unitedratings.lhcrm.service.interfaces.IdealServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class IdealServiceSVImpl implements IdealServiceSV {

    @Autowired
    private IdealDefaultDao defaultDao;

    @Autowired
    private IdealDefaultItemDao defaultItemDao;


    @Override
    public IdealDefault saveIdealDefault(IdealDefault idealDefault) {
        return defaultDao.save(idealDefault);
    }

    @Override
    public void saveIdealDefaultItemList(List<IdealDefaultItem> defaultItems) {
        defaultItemDao.save(defaultItems);
    }

    @Override
    public int getIdealDefaultCount() {
        return new Long(defaultDao.count()).intValue();
    }

    @Override
    public int getIdealDefaultItemCount() {
        return new Long(defaultItemDao.count()).intValue();
    }

    @Override
    public IdealDefault getNewestIdealDefaultTable() {
        List<IdealDefault> defaults = defaultDao.findAll(new Sort(Sort.Direction.DESC, "version"));
        if(!CollectionUtils.isEmpty(defaults)){
            return defaults.get(0);
        }
        return null;
    }

    @Override
    public List<IdealDefaultItem> getIdealDefaultItemListByIdealDefaultId(Integer id) {
        IdealDefaultItem idealDefaultItem = new IdealDefaultItem();
        idealDefaultItem.setIdealDefaultId(id);
        return defaultItemDao.findAll(Example.of(idealDefaultItem));
    }
}
