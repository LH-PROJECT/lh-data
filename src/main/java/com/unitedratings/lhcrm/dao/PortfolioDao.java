package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wangyongxin
 */
public interface PortfolioDao extends JpaRepository<Portfolio,Long>{

    /**
     * 修改资产池关联的理想违约率版本
     * @param id
     * @param idealDefaultId
     * @return
     */
    @Modifying
    @Query("update Portfolio p set p.idealDefaultId = ?2 where p.id = ?1")
    int updatePortfolioById(Long id, Integer idealDefaultId);

    /**
     * 更新资产池已模拟次数
     * @param id
     * @param simulationNum
     */
    @Modifying
    @Query("update Portfolio p set p.simulationNum = ?2 where p.id = ?1")
    void updateSimulationNum(Long id, int simulationNum);
}
