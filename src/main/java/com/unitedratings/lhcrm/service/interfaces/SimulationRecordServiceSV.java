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

    /**
     * 根据attachableId查询模拟记录次数
     * @param attachableId
     * @return
     */
    int selectCountByPortfolioId(Long attachableId);

    /**
     * 资产池已模拟记录列表
     * @param portfolioId
     * @return
     */
    List<SimulationRecord> queryRecordListByPortfolioId(Long portfolioId);
}
