package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.SysDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysDictionaryDao extends JpaRepository<SysDictionary,Integer>{
    SysDictionary findByParamCodeAndVersion(String paramCode, double version);
}
