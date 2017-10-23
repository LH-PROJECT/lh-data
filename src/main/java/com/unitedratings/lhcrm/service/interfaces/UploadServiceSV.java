package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.UploadRecord;

/**
 * @author wangyongxin
 */
public interface UploadServiceSV {
    /**
     * 保存上传记录
     * @param record
     * @return
     */
    UploadRecord save(UploadRecord record);

    /**
     * 根据上传记录id获取上传记录信息
     * @param id
     * @return
     */
    UploadRecord getUploadRecordById(Long id);
}
