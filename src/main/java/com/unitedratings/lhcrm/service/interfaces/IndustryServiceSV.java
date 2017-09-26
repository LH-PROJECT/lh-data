package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Industry;

import java.util.List;

public interface IndustryServiceSV {
    void saveIndustryList(List<Industry> industries);

    List<Industry> getIndustryLikeCode(String value);
}
