package com.unitedratings.lhcrm.web.model;

import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author wangyongxin
 */
public class AnalysisResult extends DeferredResult<PortfolioStatisticalResult> {

    private SimulationRecord record;

    public SimulationRecord getRecord() {
        return record;
    }

    public void setRecord(SimulationRecord record) {
        this.record = record;
    }
}
