package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndustryDao extends JpaRepository<Industry,Integer>{
    List<Industry> findByIndustryCodeStartingWith(String value);
}
