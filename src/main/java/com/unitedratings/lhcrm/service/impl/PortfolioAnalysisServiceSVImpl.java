package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.PortfolioAnalysisResultDao;
import com.unitedratings.lhcrm.dao.UploadRecordDao;
import com.unitedratings.lhcrm.entity.PortfolioAnalysisResult;
import com.unitedratings.lhcrm.service.interfaces.PortfolioAnalysisServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PortfolioAnalysisServiceSVImpl implements PortfolioAnalysisServiceSV {

    @Autowired
    private UploadRecordDao uploadRecordDao;

    @Autowired
    private PortfolioAnalysisResultDao portfolioAnalysisResultDao;

    @Override
    public PortfolioAnalysisResult saveAnalysisResult(PortfolioAnalysisResult analysisResult) {
        if(uploadRecordDao.exists(analysisResult.getUploadRecordId())){
            uploadRecordDao.updateRecordFinish(analysisResult.getUploadRecordId());
            analysisResult = portfolioAnalysisResultDao.save(analysisResult);
        }
        return analysisResult;
    }

    @Override
    public PortfolioAnalysisResult findLastAnalysisResultByRecordId(Long id) {
        List<PortfolioAnalysisResult> analysisResults = portfolioAnalysisResultDao.findByUploadRecordIdOrderByCreateTimeDesc(id);
        if(!CollectionUtils.isEmpty(analysisResults)){
            return analysisResults.get(0);
        }
        return null;
    }
}
