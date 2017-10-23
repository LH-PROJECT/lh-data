package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.entity.SysDictionary;
import com.unitedratings.lhcrm.service.interfaces.SysDictionaryServiceSV;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangyongxin
 */
@RestController
@RequestMapping("/dict")
public class SysDictionaryController {

    @Autowired
    private SysDictionaryServiceSV dictionaryService;

    /**
     * 获取发起机构列表
     * @return
     */
    @GetMapping("/sponsorList")
    public ResponseData<List<SysDictionary>> getSponsorList(){
        ResponseData<List<SysDictionary>> data = null;
        SysDictionary sponsorParent = dictionaryService.getDictByCodeAndVersion("ORG_TYPE", 1.0);
        if(sponsorParent!=null){
            List<SysDictionary> sponsorList = dictionaryService.getDictionaryListByParentId(sponsorParent.getId());
            data = new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"发起机构查询成功",sponsorList);
        }else {
            data = new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"发起机构不存在");
        }
        return data;
    }
}
