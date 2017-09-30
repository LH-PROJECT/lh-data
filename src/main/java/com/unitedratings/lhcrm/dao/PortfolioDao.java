package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioDao extends JpaRepository<Portfolio,Long>{

    @Modifying
    @Query("update Portfolio p set p.idealDefaultId = ?2 where p.id = ?1")
    int updatePortfolioById(Long id, Integer idealDefaultId);

    @Modifying
    @Query("update Portfolio p set p.simulationNum = ?2 where p.id = ?1")
    void updateSimulationNum(Long id, int simulationNum);
}
