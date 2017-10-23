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
