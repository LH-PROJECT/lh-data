package com.unitedratings.lhcrm.business;

import com.unitedratings.lhcrm.domains.LargeAmountResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author wangyongxin
 * @createAt 2017-10-31 下午3:44
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class LargeAmountTestCalculateTest {

    @Autowired
    private LargeAmountTestCalculate largeAmountTestCalculate;

    @Test
    public void calculate() throws Exception {
        LargeAmountResult result = largeAmountTestCalculate.calculate(53L, "AAA");
        System.out.println(result.getGrossAmountList());
        System.out.println(result.getGrossAmountListAfter5PercentRecovery());
        System.out.println(result.getMinimumSupport());
    }

}