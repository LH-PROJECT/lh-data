package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.*;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.web.model.PageModel;
import com.unitedratings.lhcrm.web.model.PageResult;
import com.unitedratings.lhcrm.web.model.PortfolioQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Override
    public Page<Portfolio> getPortfolioList(PageModel<PortfolioQuery> query) {
        PortfolioQuery portfolioQuery = query.getQuery();
        Portfolio portfolio = new Portfolio();
        if(portfolioQuery!=null){
            if(!StringUtils.isEmpty(portfolioQuery.getPortfolioName())){
                portfolio.setPortfolioName(portfolioQuery.getPortfolioName());
            }
            if(portfolioQuery.getUserId()!=null){
                portfolio.setUserId(portfolioQuery.getUserId());
            }
        }

        return portfolioDao.findAll(Example.of(portfolio), new PageRequest(query.getPageNo()-1, query.getPageSize(), Sort.Direction.DESC,"createTime"));
    }

    @Override
    public boolean deletePortfolioById(Long id) {
        Portfolio portfolio = portfolioDao.findOne(id);
        if(portfolio!=null){
            debtorInfoDao.deleteInBatch(debtorInfoDao.findByPortfolioId(id));
            guarantorInfoDao.deleteInBatch(guarantorInfoDao.findByPortfolioId(id));
            List<AmortizationInfo> amortizationInfos = amortizationInfoDao.findByPortfolioId(id);
            if(!CollectionUtils.isEmpty(amortizationInfos)){
                amortizationInfoDao.deleteInBatch(amortizationInfos);
            }
            amortizationDao.delete(id);
            portfolioDao.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<Portfolio> getPortfolioListOnSimulationRecord(PageModel<PortfolioQuery> query) {
        PortfolioQuery portfolioQuery = query.getQuery();
        List<Portfolio> list;
        PageResult<Portfolio> result= new PageResult<>();
        int count = 0;
        if(portfolioQuery.getUserId()!=null){
            if(!StringUtils.isEmpty(portfolioQuery.getPortfolioName())){
                list = portfolioDao.findPageByUserIdAndPortfolioName(portfolioQuery.getUserId(),portfolioQuery.getPortfolioName(),(query.getPageNo()-1)*query.getPageSize(),query.getPageSize());
                count = portfolioDao.countBySimulationNumGreaterThanAndUserIdAndPortfolioNameLike(0, portfolioQuery.getUserId(), portfolioQuery.getPortfolioName());
            } else {
                list = portfolioDao.findPageByUserId(portfolioQuery.getUserId(),(query.getPageNo()-1)*query.getPageSize(),query.getPageSize());
                count = portfolioDao.countBySimulationNumGreaterThanAndUserId(0,portfolioQuery.getUserId());
            }
        }else {
            if(!StringUtils.isEmpty(portfolioQuery.getPortfolioName())){
                list = portfolioDao.findPageByPortfolioName(portfolioQuery.getPortfolioName(),(query.getPageNo()-1)*query.getPageSize(),query.getPageSize());
                count = portfolioDao.countBySimulationNumGreaterThanAndPortfolioNameLike(0,portfolioQuery.getPortfolioName());
            } else {
                list = portfolioDao.findPageOrderByCreateTime((query.getPageNo()-1)*query.getPageSize(),query.getPageSize());
                count = portfolioDao.countBySimulationNumGreaterThan(0);
            }
        }
        if(count>0){
            result.setTotalRecords(count);
            result.setData(list);
            result.setPageNo(query.getPageNo());
            result.setPageSize(query.getPageSize());
            return result;
        }

        return null;
    }
}
