package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.IdealDefault;
import com.unitedratings.lhcrm.entity.IdealDefaultItem;

import java.util.List;

public interface IdealServiceSV {
    IdealDefault saveIdealDefault(IdealDefault idealDefault);

    void saveIdealDefaultItemList(List<IdealDefaultItem> defaultItems);

    int getIdealDefaultCount();

    int getIdealDefaultItemCount();

    IdealDefault getNewestIdealDefaultTable();

    List<IdealDefaultItem> getIdealDefaultItemListByIdealDefaultId(Integer id);
}
