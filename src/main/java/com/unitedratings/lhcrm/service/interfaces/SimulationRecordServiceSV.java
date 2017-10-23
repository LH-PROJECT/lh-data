package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SimulationRecord;

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
}
