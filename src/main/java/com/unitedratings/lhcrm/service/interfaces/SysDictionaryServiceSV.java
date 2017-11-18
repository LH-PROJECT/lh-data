package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SysDictionary;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface SysDictionaryServiceSV {

    /**
     * 批量保存字典项目
     * @param dictionaries
     */
    void saveDicts(List<SysDictionary> dictionaries);

    /**
     * 根据字典编码和版本查询字典信息
     * @param code
     * @param version
     * @return
     */
    SysDictionary getDictByCodeAndVersion(String code, Double version);

    /**
     * 获取字典条目数
     * @return
     */
    int getCount();

    /**
     * 保存单个字典条目
     * @param dict
     * @return
     */
    SysDictionary saveDict(SysDictionary dict);

    /**
     * 根据父级id获取所有子条目
     * @param parentId
     * @return
     */
    List<SysDictionary> getDictionaryListByParentId(Integer parentId);

    /**
     * 根据id查询条目详情
     * @param id
     * @return
     */
    SysDictionary getDictionaryById(Integer id);
}
