package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Portfolio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyongxin
 * @createAt 2017-10-24 下午4:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PortfolioServiceSVTest {

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Test
    public void getPortfolioById() throws Exception {
        Portfolio portfolio = portfolioService.getPortfolioById(50l);
        Map<String, Long> collect = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.counting()));
        System.out.println(collect);
        Map<String, DoubleSummaryStatistics> collect1 = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.summarizingDouble(r -> r.getDebtorInfo().getLoanBalance())));
        Map<String, Double> collect2 = portfolio.getRecordList().stream().collect(Collectors.groupingBy(record -> record.getDebtorInfo().getBorrowerIndustry(), Collectors.summingDouble(r -> r.getDebtorInfo().getLoanBalance())));
        System.out.println(collect1);
        System.out.println(collect2);
        portfolio.getRecordList().stream().collect(Collectors.groupingBy(record->record.getDebtorInfo().getBorrowerIndustry())).forEach((key,recordList)->{
            Map<Long, Long> collect3 = recordList.stream().collect(Collectors.groupingBy(r -> r.getDebtorInfo().getBorrowerSerial(), Collectors.counting()));
            System.out.println(collect3);
        });
    }

}