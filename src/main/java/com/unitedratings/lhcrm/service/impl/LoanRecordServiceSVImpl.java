package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.DebtorInfoDao;
import com.unitedratings.lhcrm.dao.GuarantorInfoDao;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.DebtorInfo;
import com.unitedratings.lhcrm.entity.GuarantorInfo;
import com.unitedratings.lhcrm.entity.Portfolio;
import com.unitedratings.lhcrm.service.interfaces.LoanRecordServiceSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author wangyongxin
 */
@Service
@Transactional
public class LoanRecordServiceSVImpl implements LoanRecordServiceSV {

    @Autowired
    private DebtorInfoDao debtorInfoDao;
    @Autowired
    private GuarantorInfoDao guarantorInfoDao;

}
