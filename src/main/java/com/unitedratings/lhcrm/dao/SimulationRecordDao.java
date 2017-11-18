package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.SimulationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface SimulationRecordDao extends JpaRepository<SimulationRecord,Long>{

    /**
     * 模拟完成时，更新模拟记录情况
     * @param id
     * @param consumeTime
     * @param finish
     * @param resultId
     * @return
     */
    @Modifying
    @Query("update SimulationRecord s set s.consumeTime = ?2,s.finish= ?3,s.resultId = ?4 where s.id = ?1")
    int updateSimulationRecordById(Long id, Long consumeTime, Boolean finish, Long resultId);

    /**
     * 查询已模拟完成的模拟记录
     * @param portfolioId
     * @return
     */
    List<SimulationRecord> findByAttachableIdAndFinishTrueOrderByCreateTimeDesc(Long portfolioId);

}
