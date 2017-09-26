package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SysDictionary;

import java.util.List;

public interface SysDictionaryServiceSV {

    void saveDicts(List<SysDictionary> dictionaries);

    SysDictionary getDictByCodeAndVersion(String guarantee_mode, Double version);

    int getCount();

    SysDictionary saveDict(SysDictionary dict);

    List<SysDictionary> getDictionaryListByParentId(Integer parentId);

}
