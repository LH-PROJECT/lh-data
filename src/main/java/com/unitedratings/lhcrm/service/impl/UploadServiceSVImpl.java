package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.UploadRecordDao;
import com.unitedratings.lhcrm.entity.UploadRecord;
import com.unitedratings.lhcrm.service.interfaces.UploadServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadServiceSVImpl implements UploadServiceSV {

    @Autowired
    private UploadRecordDao uploadRecordDao;

    @Override
    public UploadRecord save(UploadRecord record) {
        return uploadRecordDao.save(record);
    }

    @Override
    public UploadRecord getUploadRecordById(Long id) {
        return uploadRecordDao.findOne(id);
    }
}
