package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SimulationRecord;

public interface SimulationRecordServiceSV {
    SimulationRecord saveSimulationRecord(SimulationRecord simulationRecord);

    void updateSimulationRecord(SimulationRecord record);
}
