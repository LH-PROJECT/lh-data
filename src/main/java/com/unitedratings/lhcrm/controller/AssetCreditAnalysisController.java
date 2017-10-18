package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.annotation.NoNeedCheckLogin;
import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.config.TaskExecutorConfig;
import com.unitedratings.lhcrm.core.AnalysisResultHandler;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.entity.SimulationRecord;
import com.unitedratings.lhcrm.entity.UploadRecord;
import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.service.interfaces.SimulationRecordServiceSV;
import com.unitedratings.lhcrm.service.interfaces.UploadServiceSV;
import com.unitedratings.lhcrm.utils.DateUtil;
import com.unitedratings.lhcrm.utils.FileUtil;
import com.unitedratings.lhcrm.web.model.AnalysisResult;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

@RestController
@RequestMapping("/assetCreditAnalysis")
public class AssetCreditAnalysisController {

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private UploadServiceSV uploadService;

    @Autowired
    private TaskExecutorConfig taskExecutorConfig;

    @Autowired
    private PortfolioAnalysisServiceSV analysisService;

    @Autowired
    private PortfolioServiceSV portfolioService;

    @Autowired
    private SimulationRecordServiceSV simulationRecordService;


    /**
     * 上传待分析文件
     * @param file
     * @param portfolio
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public ResponseData<Portfolio> upload(MultipartFile file,Portfolio portfolio) throws Exception {
        ResponseData<Portfolio> responseData = null;
        if(file!=null){
            //保存上传文件
            UploadRecord record = new UploadRecord();
            record.setFileName(uploadFile(file));
            record.setCreateTime(new Date());
            //保存上传记录
            UploadRecord saved = uploadService.save(record);
            //处理excel，保存资产池信息
            portfolio.setUploadRecordId(saved.getId());
            //当前资产池已模拟次数
            portfolio.setSimulationNum(0);
            portfolio.setCurrentState("begin");
            AssetsExcelProcess.processAssetsExcel(file,portfolio);
            portfolioService.savePortfolio(portfolio);
            responseData = new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"上传成功",portfolio);
        }else {
            responseData = new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"上传文件不存在",null);
        }
        return responseData;
    }

    /**
     * 开始分析
     * @return
     */
    @PostMapping(value = "/analysis",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AnalysisResult analysis(@RequestBody SimulationRecord simulationRecord){
        AnalysisResult result = new AnalysisResult();
        Portfolio portfolio = portfolioService.getPortfolioById(simulationRecord.getAttachableId());
        if(portfolio!=null){
            simulationRecord.setFinish(false);
            simulationRecord.setCreateTime(new Date());
            simulationRecordService.saveSimulationRecord(simulationRecord);
            result.setRecord(simulationRecord);
            if(!AnalysisResultHandler.initFlag){
                synchronized (AnalysisResultHandler.class){
                    if(!AnalysisResultHandler.initFlag){
                        AnalysisResultHandler.setFileConfig(fileConfig);
                        AnalysisResultHandler.setParallelThreadNum(taskExecutorConfig.getParallelThreadNum());
                        AnalysisResultHandler.setBeginMultiThreadThreshold(taskExecutorConfig.getParallelThreadNum());
                        AnalysisResultHandler.initFlag = true;
                    }
                }
            }
            AnalysisResultHandler.addAnalysisResult(result);
        }
        return result;
    }

    /**
     * 分析结果下载
     * @param id
     * @return
     * @throws Exception
     */
    @NoNeedCheckLogin
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAnalysisResult(@PathVariable("id") Long id) throws Exception {
        PortfolioAnalysisResult analysisResult = analysisService.findLastAnalysisResultByPortfolioId(id);
        if(analysisResult!=null){
            String resultFilePath = analysisResult.getResultFilePath();
            String realFileName = analysisResult.getPortfolioId()+"_"+FileUtil.extractFileName(resultFilePath);
            String fileName = new String(realFileName.getBytes("utf-8"),"ISO-8859-1");
            File file = new File(fileConfig.getResultPath() + File.separator + resultFilePath);
            if(file.exists()){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment",fileName, Charset.forName("UTF-8"));
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.CREATED);
            }else {
                throw new FileNotFoundException("要下载的文件不存在");
            }
        }else {
           throw new Exception("下载出错");
        }
    }

    /**
     * 上传文件
     * @param file 文件
     * @return 新生成的文件名
     * @throws IOException
     */
    private String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String childPath = FileUtil.createChildPath(fileConfig.getUploadPath());
        String newFileName = childPath+File.separator+fileName.substring(0,fileName.lastIndexOf('.')) + "_" + DateUtil.getTimestamp() + suffix;
        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(new File(fileConfig.getUploadPath() +File.separator + newFileName)));
        fileOut.write(file.getBytes());
        fileOut.flush();
        fileOut.close();
        return newFileName;
    }

}
