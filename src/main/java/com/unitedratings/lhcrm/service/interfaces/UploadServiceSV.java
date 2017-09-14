package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.UploadRecord;

public interface UploadServiceSV {
    UploadRecord save(UploadRecord record);

    UploadRecord getUploadRecordById(Long id);
}
