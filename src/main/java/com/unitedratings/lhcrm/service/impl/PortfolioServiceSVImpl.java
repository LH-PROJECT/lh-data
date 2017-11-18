package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.*;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.service.interfaces.PortfolioServiceSV;
import com.unitedratings.lhcrm.web.model.LoanRecordVo;
import com.unitedratings.lhcrm.web.model.PageModel;
import com.unitedratings.lhcrm.web.model.PageResult;
import com.unitedratings.lhcrm.web.model.PortfolioQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
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
    @Autowired
    private SysDictionaryDao sysDictionaryDao;

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        SysDictionary dictionary = sysDictionaryDao.findOne(portfolio.getSponsorId());
        if(dictionary!=null){
            portfolio.setSponsorName(dictionary.getParamDesc());
        }
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
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return portfolioDao.findAll(Example.of(portfolio, matcher), new PageRequest(query.getPageNo()-1, query.getPageSize(), Sort.Direction.DESC,"createTime"));
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
    public Page<Portfolio> getPortfolioListOnSimulationRecord(PageModel<PortfolioQuery> query) {
        PortfolioQuery portfolioQuery = query.getQuery();
        if(portfolioQuery.getUserId()!=null){
            if(!StringUtils.isEmpty(portfolioQuery.getPortfolioName())){
                return portfolioDao.findByUserIdAndPortfolioNameContainsAndSimulationNumGreaterThanOrderByCreateTimeDesc(portfolioQuery.getUserId(), portfolioQuery.getPortfolioName(), 0, new PageRequest(query.getPageNo() - 1, query.getPageSize()));
            } else {
                return portfolioDao.findByUserIdAndSimulationNumGreaterThanOrderByCreateTimeDesc(portfolioQuery.getUserId(),0,new PageRequest(query.getPageNo() - 1, query.getPageSize()));
            }
        }else {
            if(!StringUtils.isEmpty(portfolioQuery.getPortfolioName())){
                return portfolioDao.findByPortfolioNameContainsAndSimulationNumGreaterThanOrderByCreateTimeDesc(portfolioQuery.getPortfolioName(),0,new PageRequest(query.getPageNo() - 1, query.getPageSize()));
            } else {
                return portfolioDao.findBySimulationNumGreaterThanOrderByCreateTimeDesc(0,new PageRequest(query.getPageNo() - 1, query.getPageSize()));
            }
        }
    }

    @Override
    public Portfolio getFullPortfolioInfoById(Long id) {
        Portfolio portfolio = getPortfolioById(id);
        if(portfolio!=null){
            Amortization amortization = amortizationDao.findOne(id);
            if(amortization!=null){
                List<AmortizationInfo> amortizationInfos = amortizationInfoDao.findByPortfolioId(id);
                amortization.setAmortizationInfoList(amortizationInfos);
                portfolio.setAmortization(amortization);
            }
        }
        return portfolio;
    }

    @Override
    public boolean updateLoanRecord(LoanRecordVo recordVo) throws BusinessException {
        try {
            DebtorInfo debtorInfo = debtorInfoDao.findOne(recordVo.getDebtInfoId());
            GuarantorInfo guarantorInfo = guarantorInfoDao.findOne(recordVo.getGuarantorInfoId());
            Field[] fields = recordVo.getClass().getDeclaredFields();
            List<String> ignoreProps = new ArrayList<>();
            for(Field field:fields){
                field.setAccessible(true);
                if(StringUtils.isEmpty(field.get(recordVo))){
                    ignoreProps.add(field.getName());
                }
            }
            BeanUtils.copyProperties(recordVo,debtorInfo,ignoreProps.toArray(new String[0]));
            debtorInfoDao.save(debtorInfo);
            BeanUtils.copyProperties(recordVo,guarantorInfo,ignoreProps.toArray(new String[0]));
            guarantorInfoDao.save(guarantorInfo);
            return true;
        } catch (IllegalAccessException e) {
            throw new BusinessException("000009","更新贷款记录信息失败",e);
        }
    }

    @Override
    public boolean updateAmortization(AmortizationInfo info) {
        AmortizationInfo saved = amortizationInfoDao.save(info);
        return saved==null?false:true;
    }
}
