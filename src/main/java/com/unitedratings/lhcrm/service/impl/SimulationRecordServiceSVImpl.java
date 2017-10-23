package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.SimulationRecordDao;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyongxin
 */
@Service
@Transactional
public class SimulationRecordServiceSVImpl implements SimulationRecordServiceSV {

    @Autowired
    private SimulationRecordDao simulationRecordDao;

    @Override
    public SimulationRecord saveSimulationRecord(SimulationRecord simulationRecord) {
        return simulationRecordDao.save(simulationRecord);
    }

    @Override
    public void updateSimulationRecord(SimulationRecord record) {
        simulationRecordDao.updateSimulationRecordById(record.getId(),record.getConsumeTime(),record.getFinish(),record.getResultId());
    }
}
