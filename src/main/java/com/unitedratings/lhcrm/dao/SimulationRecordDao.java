package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.SimulationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SimulationRecordDao extends JpaRepository<SimulationRecord,Long>{

    @Modifying
    @Query("update SimulationRecord s set s.consumeTime = ?2,s.finish= ?3,s.resultId = ?4 where s.id = ?1")
    int updateSimulationRecordById(Long id, Long consumeTime, Boolean finish, Long resultId);
}
