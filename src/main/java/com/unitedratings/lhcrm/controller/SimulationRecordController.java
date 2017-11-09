package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.web.model.PageResult;
import com.unitedratings.lhcrm.web.model.PortfolioVo;
import com.unitedratings.lhcrm.web.model.ResponseData;
import com.unitedratings.lhcrm.web.model.SimulationRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-11-09 下午3:58
 **/
@RestController
@RequestMapping("/simulationRecord")
public class SimulationRecordController {

    @Autowired
    private SimulationRecordServiceSV simulationRecordService;

    @PostMapping("/list")
    public ResponseData<PageResult<PortfolioVo>> recordList(@RequestBody SimulationRecordQuery query){
        List<SimulationRecord> records = simulationRecordService.queryRecordListGroupByPortfolioId(query);
        return null;
    }
}
