package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.SimulationRecordDao;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.web.model.SimulationRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @Override
    public List<SimulationRecord> queryRecordListGroupByPortfolioId(SimulationRecordQuery query) {
        SimulationRecord record = new SimulationRecord();
        if(query.getAttachableId()!=null){
            record.setAttachableId(query.getAttachableId());
        }
        if(!StringUtils.isEmpty(query.getAttachableType())){
            record.setAttachableType(query.getAttachableType());
        }
        if(query.getUserId()!=null){
            record.setUserId(query.getUserId());
        }
        simulationRecordDao.findAll(Example.of(record));
        return null;
    }
}
