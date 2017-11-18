package com.unitedratings.lhcrm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author wangyongxin
 */
@Entity
public class IdealDefault {

    @Id
    @GeneratedValue
    private Integer id;
    private Double version;
    private Date createTime;
    @Transient
    private List<IdealDefaultItem> defaultItemList;

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

    public List<IdealDefaultItem> getDefaultItemList() {
        return defaultItemList;
    }

    public void setDefaultItemList(List<IdealDefaultItem> defaultItemList) {
        this.defaultItemList = defaultItemList;
    }
}
