package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.LargeAmountSettings; /**
 * @author wangyongxin
 * @createAt 2017-10-30 下午3:37
 **/
public interface LargeAmountSettingsService {
    /**
     * 保存大额预置设置信息
     * @param largeAmountSettings
     * @return
     */
    LargeAmountSettings saveSettings(LargeAmountSettings largeAmountSettings);

    /**
     * 获取最新大额测试预置设置
     * @return
     */
    LargeAmountSettings getNewestSettings();
}
