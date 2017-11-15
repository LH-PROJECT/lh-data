package com.unitedratings.lhcrm.web.model;

import com.unitedratings.lhcrm.entity.SimulationRecord;

/**
 * @author wangyongxin
 * @createAt 2017-11-13 下午4:23
 **/
public class SimulationRecordVo extends SimulationRecord{
    private UserModel user;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
