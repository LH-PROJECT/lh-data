package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.UploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangyongxin
 */
public interface UploadRecordDao extends JpaRepository<UploadRecord,Long>{
}
