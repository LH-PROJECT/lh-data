package com.unitedratings.lhcrm.core;

import com.unitedratings.lhcrm.config.ExecutorThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyongxin
 */
@Component
@EnableConfigurationProperties(ExecutorThreadPoolConfig.class)
public class ExecutorEngine {

    private static volatile ThreadPoolExecutor executor;

    private ExecutorEngine(){}

    @Autowired
    private ExecutorThreadPoolConfig config;

    @PostConstruct
    public boolean initPool(){
        if(executor==null){
            synchronized (ExecutorEngine.class){
                executor = new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaxPoolSize(), config.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingQueue<>(config.getQueueCapacity()));
            }
        }
        return true;
    }

    public static ExecutorService getExecutorEngine(){
        return executor;
    }

}
