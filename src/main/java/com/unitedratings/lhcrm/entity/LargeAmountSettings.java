package com.unitedratings.lhcrm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author wangyongxin
 * @createAt 2017-10-30 下午3:13
 **/
@Entity
public class LargeAmountSettings {

    @Id
    @GeneratedValue
    private Integer id;
    private Double version;
    private Date createTime;
    @Column(length = 1500)
    private String settingsDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSettingsDetail() {
        return settingsDetail;
    }

    public void setSettingsDetail(String settingsDetail) {
        this.settingsDetail = settingsDetail;
    }
}
