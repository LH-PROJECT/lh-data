package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.GuarantorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuarantorInfoDao extends JpaRepository<GuarantorInfo,Long>{
    List<GuarantorInfo> findByPortfolioId(Long id);
}
