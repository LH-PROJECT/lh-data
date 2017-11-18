package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.SysDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangyongxin
 */
public interface SysDictionaryDao extends JpaRepository<SysDictionary,Integer>{
    /**
     * 根据字典编码和版本查询字典详情
     * @param paramCode 字典编码
     * @param version 版本
     * @return
     */
    SysDictionary findByParamCodeAndVersion(String paramCode, double version);
}
