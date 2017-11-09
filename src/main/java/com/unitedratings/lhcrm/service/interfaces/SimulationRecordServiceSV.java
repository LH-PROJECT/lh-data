package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.web.model.SimulationRecordQuery;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface SimulationRecordServiceSV {
    /**
     * 保存模拟记录
     * @param simulationRecord
     * @return
     */
    SimulationRecord saveSimulationRecord(SimulationRecord simulationRecord);

    /**
     * 更新模拟记录
     * @param record
     */
    void updateSimulationRecord(SimulationRecord record);

    /**
     * 按资产池id分组查询模拟记录
     * @param query
     * @return
     */
    List<SimulationRecord> queryRecordListGroupByPortfolioId(SimulationRecordQuery query);
}
