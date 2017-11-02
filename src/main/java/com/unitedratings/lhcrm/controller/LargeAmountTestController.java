package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.business.LargeAmountTestCalculate;
import com.unitedratings.lhcrm.domains.LargeAmountResult;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseData<LargeAmountResult> calculate(){
        Long portfolioId = 53L;
        String CreditLevel = "AAA";
        LargeAmountResult result = largeAmountTestCalculate.calculate(portfolioId,CreditLevel);
        return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"计算成功",result);
    }

}
