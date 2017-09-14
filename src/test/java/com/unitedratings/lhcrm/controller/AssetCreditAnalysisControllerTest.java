package com.unitedratings.lhcrm.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class AssetCreditAnalysisControllerTest {

    @Test
    public void analysis() throws Exception {
        long t1 = System.currentTimeMillis();
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://127.0.0.1:8081/assetCreditAnalysis/analysis/1", String.class);
        System.out.println(entity);
        long t2 = System.currentTimeMillis();
        System.out.println("耗时："+(t2-t1)+"ms");
    }

}