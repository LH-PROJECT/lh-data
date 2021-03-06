package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.business.LargeAmountTestCalculate;
import com.unitedratings.lhcrm.domains.LargeAmountResult;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author wangyongxin
 * @createAt 2017-10-30 上午11:46
 **/
@RestController
@RequestMapping("/largeAmountTest")
public class LargeAmountTestController {

    @Autowired
    private LargeAmountTestCalculate largeAmountTestCalculate;

    @PostMapping("/calculate")
    public ResponseData<LargeAmountResult> calculate(Long portfolioId, String creditLevel) throws BusinessException {
        if(portfolioId==null){
            return new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"资产池id不能为空");
        }
        if(StringUtils.isEmpty(creditLevel)){
            return new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"信用级别不能为空");
        }
        LargeAmountResult result = largeAmountTestCalculate.calculate(portfolioId,creditLevel);
        return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"计算成功",result);
    }

    @GetMapping("/calculateAllLevel/{portfolioId}")
    public ResponseData<List<LargeAmountResult>> calculateAllLevel(@PathVariable("portfolioId") Long portfolioId) throws BusinessException {
        if(portfolioId==null){
            return new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"资产池id不能为空");
        }
        List<LargeAmountResult> results = largeAmountTestCalculate.calculateAllLevel(portfolioId);
        return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"计算成功",results);
    }

}
