package com.unitedratings.lhcrm.entity;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "idealDefaultId")})
public class IdealDefaultItem {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer rankId;
    @Column(length = 30)
    private String rankCode;
    private Integer life;
    private Double defaultRate;
    private Integer idealDefaultId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Double getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(Double defaultRate) {
        this.defaultRate = defaultRate;
    }

    public Integer getIdealDefaultId() {
        return idealDefaultId;
    }

    public void setIdealDefaultId(Integer idealDefaultId) {
        this.idealDefaultId = idealDefaultId;
    }
}
