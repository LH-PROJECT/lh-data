package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.entity.User;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.service.interfaces.UserServiceSV;
import com.unitedratings.lhcrm.web.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Autowired
    private UserServiceSV userService;

    /**
     * 已模拟资产池列表
     * @param query
     * @return
     */
    @PostMapping("/portfolioList")
    public ResponseData<PageResult<PortfolioVo>> portfolioList(@RequestBody PageModel<PortfolioQuery> query){
        Page<Portfolio> result = portfolioService.getPortfolioListOnSimulationRecord(query);
        if(result!=null){
            PageResult<PortfolioVo> pageResult = new PageResult<>();
            Page<PortfolioVo> portfolioVos = result.map(portfolio -> {
                PortfolioVo portfolioVo = new PortfolioVo();
                BeanUtils.copyProperties(portfolio, portfolioVo);
                if (portfolio.getUserId() != null) {
                    User user = userService.getUserById(portfolio.getUserId());
                    if (user != null) {
                        UserModel userModel = new UserModel();
                        userModel.setId(user.getId());
                        userModel.setDisplayName(user.getDisplayName());
                        userModel.setUsername(user.getUsername());
                        portfolioVo.setUser(userModel);
                    }
                }
                return portfolioVo;
            });
            pageResult.setData(portfolioVos.getContent());
            pageResult.setTotalRecords((int) portfolioVos.getTotalElements());
            pageResult.setPageNo(portfolioVos.getNumber()+1);
            pageResult.setPageSize(portfolioVos.getSize());
            return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"结果查询成功",pageResult);
        }else {
            return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"未查询到符合条件的记录",null);
        }
    }

    /**
     * 资产池已模拟记录列表
     * @param portfolioId 资产池id
     * @return
     */
    @GetMapping("/list/{portfolioId}")
    public ResponseData<List<SimulationRecordVo>> recordList(@PathVariable("portfolioId") Long portfolioId){
        if(portfolioId!=null){
            List<SimulationRecord> records = simulationRecordService.queryRecordListByPortfolioId(portfolioId);
            if(!CollectionUtils.isEmpty(records)){
                List<SimulationRecordVo> vos = new ArrayList<>(records.size());
                records.forEach(record->{
                    SimulationRecordVo simulationRecordVo = new SimulationRecordVo();
                    BeanUtils.copyProperties(record,simulationRecordVo);
                    if(record.getUserId()!=null){
                        User user = userService.getUserById(record.getUserId());
                        if (user != null) {
                            UserModel userModel = new UserModel();
                            userModel.setId(user.getId());
                            userModel.setDisplayName(user.getDisplayName());
                            userModel.setUsername(user.getUsername());
                            simulationRecordVo.setUser(userModel);
                        }
                    }
                    vos.add(simulationRecordVo);
                });
                return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"查询成功",vos);
            }else {
                return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"查询成功,结果不存在");
            }
        }else {
            return new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"查询失败，资产池id不能为空");
        }
    }
}
