package com.unitedratings.lhcrm.domains;

import com.unitedratings.lhcrm.entity.DebtorInfo;
import com.unitedratings.lhcrm.entity.GuarantorInfo;

public class LoanRecord {

    private DebtorInfo debtorInfo;

    private GuarantorInfo guarantorInfo;

    public DebtorInfo getDebtorInfo() {
        return debtorInfo;
    }

    public void setDebtorInfo(DebtorInfo debtorInfo) {
        this.debtorInfo = debtorInfo;
    }

    public GuarantorInfo getGuarantorInfo() {
        return guarantorInfo;
    }

    public void setGuarantorInfo(GuarantorInfo guarantorInfo) {
        this.guarantorInfo = guarantorInfo;
    }
}
