package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.config.TaskExecutorConfig;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.core.AnalysisResultHandler;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.entity.UploadRecord;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import com.unitedratings.lhcrm.service.interfaces.UploadServiceSV;
import com.unitedratings.lhcrm.utils.DateUtil;
import com.unitedratings.lhcrm.web.model.AnalysisResult;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/assetCreditAnalysis")
public class AssetCreditAnalysisController {

    @Autowired
    private UploadServiceSV uploadService;

    @Autowired
    private TaskExecutorConfig taskExecutorConfig;

    @Autowired
    private PortfolioAnalysisServiceSV analysisService;

    /**
     * 上传待分析文件
     * @param file
     * @param record
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public ResponseData<Long> upload(MultipartFile file,UploadRecord record) throws IOException {
        ResponseData<Long> responseData = null;
        if(file!=null){
            record.setFileName(uploadFile(file));
            record.setCreateTime(new Date());
            UploadRecord saved = uploadService.save(record);
            responseData = new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"上传成功",saved.getId());
        }else {
            responseData = new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"上传文件不存在",null);
        }
        return responseData;
    }

    /**
     * 开始分析
     * @param id
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @GetMapping("/analysis/{id}")
    public DeferredResult analysis(@PathVariable("id") Long id) throws IOException, InvalidFormatException {
        AnalysisResult result = new AnalysisResult();
        UploadRecord record = uploadService.getUploadRecordById(id);
        if(record!=null){
            record.setSummaryType(2);
            record.setBeginCalculateDate(Date.from(LocalDate.of(2017,5,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            result.setRecord(record);
            AnalysisResultHandler.setParallelThreadNum(taskExecutorConfig.getParallelThreadNum());
            AnalysisResultHandler.setBeginMultiThreadThreshold(taskExecutorConfig.getParallelThreadNum());
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
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAnalysisResult(@PathVariable("id") Long id) throws Exception {
        PortfolioAnalysisResult analysisResult = analysisService.findLastAnalysisResultByRecordId(id);
        if(analysisResult!=null){
            String resultFilePath = analysisResult.getResultFilePath();
            String fileName = new String(resultFilePath.getBytes("utf-8"),"ISO-8859-1");
            File file = new File(Constant.RESULT_PATH + File.separator + resultFilePath);
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
        String newFileName = fileName.substring(0,fileName.lastIndexOf('.')) + "_" + DateUtil.getTimestamp() + suffix;
        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(new File(Constant.UPLOAD_PATH +File.separator + newFileName)));
        fileOut.write(file.getBytes());
        fileOut.flush();
        fileOut.close();
        return newFileName;
    }

}
