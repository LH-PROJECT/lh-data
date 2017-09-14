package com.unitedratings.lhcrm.core;

import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.entity.UploadRecord;
import com.unitedratings.lhcrm.web.model.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 蒙特卡洛模拟处理器
 */
@Component
public class AnalysisResultHandler extends LoopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisResultHandler.class);

    private static final ConcurrentLinkedQueue<AnalysisResult> queue = new ConcurrentLinkedQueue();

    private static final ConcurrentHashMap<Long,PortfolioStatisticalResult> resultMap = new ConcurrentHashMap();

    private volatile AnalysisResultHandler handler;

    private static int parallelThreadNum;

    private static int beginMultiThreadThreshold;

    @PostConstruct
    public void startUp(){
        if(handler == null){
            synchronized (this){
                handler = new AnalysisResultHandler();
            }
        }
        handler.start();
    }

    public static void addAnalysisResult(AnalysisResult result){
        queue.offer(result);
    }

    @Override
    boolean init() {
        return true;
    }

    @Override
    void unInit() {

    }

    @Override
    void work() {
        AnalysisResult analysisResult = queue.poll();
        if(analysisResult!=null){
            ExecutorService executorEngine = ExecutorEngine.getExecutorEngine();
            executorEngine.execute(()->{
                try {
                    long begin = System.currentTimeMillis();
                    UploadRecord record = analysisResult.getRecord();
                    Future<PortfolioStatisticalResult> monteResultFuture = executorEngine.submit(new AnalysisResultMerge(record,parallelThreadNum,beginMultiThreadThreshold));
                    PortfolioStatisticalResult portfolioStatisticalResult = monteResultFuture.get();
                    resultMap.put(record.getId(),portfolioStatisticalResult);
                    analysisResult.setResult(portfolioStatisticalResult);
                    long end = System.currentTimeMillis();
                    LOGGER.info("任务[id={}]模拟执行耗时{}ms",record.getId(),end-begin);
                } catch (InterruptedException|ExecutionException e) {
                    LOGGER.error("模拟过程异常",e);
                }
            });
        }
    }

    public static PortfolioStatisticalResult getPortfolioStatisticalResult(Long id){
        return resultMap.remove(id);
    }

    public static void setParallelThreadNum(int parallelThreadNum) {
        AnalysisResultHandler.parallelThreadNum = parallelThreadNum;
    }

    public static void setBeginMultiThreadThreshold(int beginMultiThreadThreshold) {
        AnalysisResultHandler.beginMultiThreadThreshold = beginMultiThreadThreshold;
    }
}
