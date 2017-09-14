package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.UploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UploadRecordDao extends JpaRepository<UploadRecord,Long>{

    @Modifying
    @Query("update UploadRecord r set r.finish = true where r.id = ?1")
    int updateRecordFinish(Long uploadRecordId);
}
