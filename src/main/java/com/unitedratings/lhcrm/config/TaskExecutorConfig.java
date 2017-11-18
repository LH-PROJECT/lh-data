package com.unitedratings.lhcrm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangyongxin
 */
@Component
@ConfigurationProperties(prefix = "task")
public class TaskExecutorConfig {

    private int parallelThreadNum;

    /**
     * 开启多线程阈值（单位：万次）
     * 计算方法：阈值 = 模拟次数（万次） * 资产笔数 * 到期期数均值
     */
    private int beginMultiThreadThreshold;

    public int getParallelThreadNum() {
        return parallelThreadNum;
    }

    public void setParallelThreadNum(int parallelThreadNum) {
        this.parallelThreadNum = parallelThreadNum;
    }

    public int getBeginMultiThreadThreshold() {
        return beginMultiThreadThreshold;
    }

    public void setBeginMultiThreadThreshold(int beginMultiThreadThreshold) {
        this.beginMultiThreadThreshold = beginMultiThreadThreshold;
    }
}
