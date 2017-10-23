package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.*;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyongxin
 */
@Service
@Transactional
public class PortfolioServiceSVImpl implements PortfolioServiceSV {

    @Autowired
    private PortfolioDao portfolioDao;
    @Autowired
    private DebtorInfoDao debtorInfoDao;
    @Autowired
    private GuarantorInfoDao guarantorInfoDao;
    @Autowired
    private AmortizationDao amortizationDao;
    @Autowired
    private AmortizationInfoDao amortizationInfoDao;

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        portfolioDao.save(portfolio);
        if(!CollectionUtils.isEmpty(portfolio.getRecordList())){
            for(LoanRecord loanRecord:portfolio.getRecordList()){
                DebtorInfo debtorInfo = loanRecord.getDebtorInfo();
                GuarantorInfo guarantorInfo = loanRecord.getGuarantorInfo();
                debtorInfo.setPortfolioId(portfolio.getId());
                guarantorInfo.setPortfolioId(portfolio.getId());
                debtorInfoDao.save(debtorInfo);
                guarantorInfoDao.save(guarantorInfo);
            }
        }
        Amortization amortization = portfolio.getAmortization();
        if(amortization !=null){
            amortization.setPortfolioId(portfolio.getId());
            amortizationDao.save(amortization);
            List<AmortizationInfo> amortizationInfoList = amortization.getAmortizationInfoList();
            if(!CollectionUtils.isEmpty(amortizationInfoList)){
                for (AmortizationInfo amortizationInfo: amortizationInfoList){
                    amortizationInfo.setPortfolioId(portfolio.getId());
                }
                amortizationInfoDao.save(amortizationInfoList);
            }
        }
        return portfolio;
    }

    @Override
    public Portfolio getPortfolioById(Long id) {
        Portfolio portfolio = portfolioDao.findOne(id);
        if(portfolio!=null){
            List<LoanRecord> loanRecords = new ArrayList<>();
            List<DebtorInfo> debtorInfoList = debtorInfoDao.findByPortfolioId(portfolio.getId());
            List<GuarantorInfo> guarantorInfoList = guarantorInfoDao.findByPortfolioId(portfolio.getId());
            for(int i=0;i<debtorInfoList.size();i++){
                LoanRecord loanRecord = new LoanRecord();
                loanRecord.setDebtorInfo(debtorInfoList.get(i));
                loanRecord.setGuarantorInfo(guarantorInfoList.get(i));
                loanRecords.add(loanRecord);
            }
            portfolio.setRecordList(loanRecords);
        }
        return portfolio;
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        portfolioDao.updatePortfolioById(portfolio.getId(), portfolio.getIdealDefaultId());
        for(LoanRecord record:portfolio.getRecordList()){
            debtorInfoDao.save(record.getDebtorInfo());
            guarantorInfoDao.save(record.getGuarantorInfo());
        }
    }
}
