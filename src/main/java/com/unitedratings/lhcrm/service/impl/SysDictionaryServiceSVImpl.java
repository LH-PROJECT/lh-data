package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.SysDictionaryDao;
import com.unitedratings.lhcrm.entity.SysDictionary;
import com.unitedratings.lhcrm.service.interfaces.SysDictionaryServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysDictionaryServiceSVImpl implements SysDictionaryServiceSV {

    @Autowired
    private SysDictionaryDao dictionaryDao;

    @Override
    public void saveDicts(List<SysDictionary> dictionaries) {
        dictionaryDao.save(dictionaries);
    }

    @Override
    public SysDictionary getDictByCodeAndVersion(String paramCode, Double version) {
        return dictionaryDao.findByParamCodeAndVersion(paramCode,version);
    }

    @Override
    public int getCount() {
        return new Long(dictionaryDao.count()).intValue();
    }

    @Override
    public SysDictionary saveDict(SysDictionary dict) {
        return dictionaryDao.save(dict);
    }

    @Override
    public List<SysDictionary> getDictionaryListByParentId(Integer parentId) {
        SysDictionary dictionary = new SysDictionary();
        dictionary.setParentId(parentId);
        dictionary.setDel(false);
        dictionary.setValid(true);
        return dictionaryDao.findAll(Example.of(dictionary));
    }

}
