package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Industry;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface IndustryServiceSV {
    /**
     * 批量保存行业
     * @param industries
     */
    void saveIndustryList(List<Industry> industries);

    /**
     * 根据行业代码查询行业信息
     * @param value 行业代码
     * @return
     */
    List<Industry> getIndustryLikeCode(String value);
}
