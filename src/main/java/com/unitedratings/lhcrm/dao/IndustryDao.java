package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface IndustryDao extends JpaRepository<Industry,Integer>{
    /**
     * 根据前缀查询行业代码
     * @param value 行业前缀
     * @return
     */
    List<Industry> findByIndustryCodeStartingWith(String value);
}
