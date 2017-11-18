package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.IdealDefault;
import com.unitedratings.lhcrm.entity.IdealDefaultItem;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface IdealServiceSV {
    /**
     * 保存理想违约率
     * @param idealDefault
     * @return
     */
    IdealDefault saveIdealDefault(IdealDefault idealDefault);

    /**
     * 保存理想违约率条目
     * @param defaultItems
     */
    void saveIdealDefaultItemList(List<IdealDefaultItem> defaultItems);

    /**
     * 获取理想违约率数
     * @return
     */
    int getIdealDefaultCount();

    /**
     * 获取理想违约率条目数
     * @return
     */
    int getIdealDefaultItemCount();

    /**
     * 获取最新版理想违约率
     * @return
     */
    IdealDefault getNewestIdealDefaultTable();

    /**
     * 根据理想违约率id获取获取其包含的理想违约率条目列表
     * @param id
     * @return
     */
    List<IdealDefaultItem> getIdealDefaultItemListByIdealDefaultId(Integer id);
}
