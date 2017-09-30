package com.unitedratings.lhcrm.web.model;

import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.entity.UploadRecord;
import org.springframework.web.context.request.async.DeferredResult;

public class AnalysisResult extends DeferredResult<PortfolioStatisticalResult> {

    private UploadRecord record;

    public UploadRecord getRecord() {
        return record;
    }

    public void setRecord(UploadRecord record) {
        this.record = record;
    }

}
