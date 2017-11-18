package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.SimulationRecordDao;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.web.model.SimulationRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return null;
    }

    @Override
    public int selectCountByPortfolioId(Long attachableId) {
        SimulationRecord simulationRecord = new SimulationRecord();
        simulationRecord.setAttachableId(attachableId);
        return (int) simulationRecordDao.count(Example.of(simulationRecord));
    }

    @Override
    public List<SimulationRecord> queryRecordListByPortfolioId(Long portfolioId) {
        return simulationRecordDao.findByAttachableIdAndFinishTrueOrderByCreateTimeDesc(portfolioId);
    }
}
