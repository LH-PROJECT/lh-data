package com.unitedratings.lhcrm.core;

import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.domains.PortfolioStatisticalResult;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.utils.SpringApplicationContextUtil;
import com.unitedratings.lhcrm.web.model.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 蒙特卡洛模拟处理器
 * @author wangyongxin
 */
@Component
public class AnalysisResultHandler extends LoopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisResultHandler.class);

    private static final ConcurrentLinkedQueue<AnalysisResult> queue = new ConcurrentLinkedQueue();

    private static final ConcurrentHashMap<Long,PortfolioStatisticalResult> resultMap = new ConcurrentHashMap();

    private volatile AnalysisResultHandler handler;

    public static volatile boolean initFlag = false;

    private static int parallelThreadNum;

    private static int beginMultiThreadThreshold;

    private static FileConfig fileConfig;

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
                    SimulationRecord record = analysisResult.getRecord();
                    Future<PortfolioStatisticalResult> monteResultFuture = executorEngine.submit(new AnalysisResultMerge(record,parallelThreadNum,beginMultiThreadThreshold,fileConfig));
                    PortfolioStatisticalResult portfolioStatisticalResult = monteResultFuture.get();
                    resultMap.put(record.getId(),portfolioStatisticalResult);
                    analysisResult.setResult(portfolioStatisticalResult);
                    record.setResult(portfolioStatisticalResult);
                    long end = System.currentTimeMillis();
                    record.setConsumeTime(end-begin);
                    record.setFinish(true);
                    saveSimulationRecord(record);
                    LOGGER.info("任务[id={}]模拟执行耗时{}ms",record.getId(),end-begin);
                } catch (InterruptedException|ExecutionException e) {
                    LOGGER.error("模拟过程异常",e);
                    analysisResult.setErrorResult(e);
                }
            });
        }
    }

    private void saveSimulationRecord(SimulationRecord record) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getContext();
        SimulationRecordServiceSV simulationRecordService = applicationContext.getBean(SimulationRecordServiceSV.class);
        simulationRecordService.updateSimulationRecord(record);
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

    public static void setFileConfig(FileConfig fileConfig) {
        AnalysisResultHandler.fileConfig = fileConfig;
    }
}
