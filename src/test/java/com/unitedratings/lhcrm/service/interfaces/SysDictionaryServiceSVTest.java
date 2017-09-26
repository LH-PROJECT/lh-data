package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.SysDictionary;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.jvm.hotspot.memory.Dictionary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysDictionaryServiceSVTest {

    @Autowired
    private SysDictionaryServiceSV sysDictionaryService;

    @Test
    public void initData() throws InvalidFormatException, IOException {
        Date createTime = new Date();
        List<SysDictionary> dictionaries = new ArrayList<>();
        SysDictionary dictionary = new SysDictionary();
        dictionary.setCreateTime(createTime);
        dictionary.setDel(false);
        dictionary.setValid(true);
        dictionary.setParamCode("GUARANTEE_MODE");
        dictionary.setVersion(1.0);
        dictionary.setParamDesc("担保方式");
        dictionaries.add(dictionary);
        SysDictionary dictionary1 = new SysDictionary();
        dictionary1.setCreateTime(createTime);
        dictionary1.setDel(false);
        dictionary1.setValid(true);
        dictionary1.setParamCode("CREDIT_LEVEL");
        dictionary1.setVersion(1.0);
        dictionary1.setParamDesc("信用级别");
        dictionaries.add(dictionary1);
        SysDictionary dictionary2 = new SysDictionary();
        dictionary2.setCreateTime(createTime);
        dictionary2.setDel(false);
        dictionary2.setValid(true);
        dictionary2.setParamCode("ORG_TYPE");
        dictionary2.setVersion(1.0);
        dictionary2.setParamDesc("机构类型");
        dictionaries.add(dictionary2);
        sysDictionaryService.saveDicts(dictionaries);
    }

    @Test
    public void init() throws InvalidFormatException, IOException {
        File file = new File("/Users/wangyongxin/Downloads/CLO国兴租赁终版.xlsm");
        final OPCPackage pkg = OPCPackage.open(file);
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        XSSFSheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets()-1);
        Date date = new Date();
        SysDictionary dict = sysDictionaryService.getDictByCodeAndVersion("GUARANTEE_MODE",1.0);
        SysDictionary dict1 = sysDictionaryService.getDictByCodeAndVersion("CREDIT_LEVEL",1.0);
        SysDictionary dict2 = sysDictionaryService.getDictByCodeAndVersion("ORG_TYPE",1.0);
        List<SysDictionary> dictionaries = new ArrayList<>();
        for(int r = 1;r<sheet.getLastRowNum();r++){
            XSSFRow row = sheet.getRow(r);
            if(row!=null){
                for(int c=0;c<row.getLastCellNum();c++){
                    XSSFCell cell = row.getCell(c);
                    if(c==0&&cell!=null){
                        if(r<4){
                            SysDictionary dictionary = new SysDictionary();
                            dictionary.setVersion(1.0);
                            dictionary.setDel(false);
                            dictionary.setValid(true);
                            dictionary.setCreateTime(date);
                            dictionary.setParamOrder(r);
                            dictionary.setParentId(dict.getId());
                            dictionary.setParamValue(String.valueOf(r));
                            dictionary.setParamDesc(cell.getStringCellValue());
                            if(r==1){
                                dictionary.setParamCode("credit_loan");
                            }else if(r==2){
                                dictionary.setParamCode("guarantee");
                            }else if(r==3){
                                dictionary.setParamCode("mortgage");
                            }else {
                                continue;
                            }
                            dictionaries.add(dictionary);
                        }
                    }else if(c==1&&cell!=null){
                        SysDictionary dictionary = new SysDictionary();
                        dictionary.setVersion(1.0);
                        dictionary.setDel(false);
                        dictionary.setValid(true);
                        dictionary.setCreateTime(date);
                        dictionary.setParamOrder(r);
                        dictionary.setParentId(dict1.getId());
                        dictionary.setParamValue(String.valueOf(r));
                        dictionary.setParamCode(cell.getStringCellValue());
                        dictionaries.add(dictionary);
                    }else if(c==4&&cell!=null){
                        if(r>5){
                            break;
                        }
                        SysDictionary dictionary = new SysDictionary();
                        dictionary.setVersion(1.0);
                        dictionary.setDel(false);
                        dictionary.setValid(true);
                        dictionary.setCreateTime(date);
                        dictionary.setParamOrder(r);
                        dictionary.setParentId(dict2.getId());
                        dictionary.setParamValue(String.valueOf(r));
                        if(r==1){
                            dictionary.setParamCode("zcx");
                        }else if(r==2){
                            dictionary.setParamCode("gysy");
                        }else if(r==3) {
                            dictionary.setParamCode("gfzsy");
                        }else if(r==4){
                            dictionary.setParamCode("dfsyjwz");
                        }else if(r==5){
                            dictionary.setParamCode("other");
                        }
                        dictionary.setParamDesc(cell.getStringCellValue());
                        dictionaries.add(dictionary);
                    }else {
                        continue;
                    }
                }
            }
        }
        sysDictionaryService.saveDicts(dictionaries);
        pkg.close();
    }

    @Test
    public void initCorrelation(){
        SysDictionary dict = new SysDictionary();
        Date createTime = new Date();
        dict.setCreateTime(createTime);
        dict.setDel(false);
        dict.setValid(true);
        dict.setParamCode("BASE_COEFFICIENT");
        dict.setVersion(1.0);
        dict.setParamDesc("资产基准相关系数");
        sysDictionaryService.saveDict(dict);

        SysDictionary dict1 = new SysDictionary();
        dict1.setCreateTime(createTime);
        dict1.setDel(false);
        dict1.setValid(true);
        dict1.setParamCode("sameArea_sameIndustry");
        dict1.setParamValue(String.valueOf(0.4));
        dict1.setVersion(1.0);
        dict1.setParamDesc("相同地区、相同行业");
        dict1.setParentId(dict.getId());
        SysDictionary dict2 = new SysDictionary();
        dict2.setCreateTime(createTime);
        dict2.setDel(false);
        dict2.setValid(true);
        dict2.setParamCode("sameArea_diffIndustry");
        dict2.setParamValue(String.valueOf(0.25));
        dict2.setVersion(1.0);
        dict2.setParamDesc("相同地区、不同行业");
        dict2.setParentId(dict.getId());
        SysDictionary dict3 = new SysDictionary();
        dict3.setCreateTime(createTime);
        dict3.setDel(false);
        dict3.setValid(true);
        dict3.setParamCode("diffArea_sameIndustry");
        dict3.setParamValue(String.valueOf(0.35));
        dict3.setVersion(1.0);
        dict3.setParamDesc("不同地区、相同行业");
        dict3.setParentId(dict.getId());
        SysDictionary dict4 = new SysDictionary();
        dict4.setCreateTime(createTime);
        dict4.setDel(false);
        dict4.setValid(true);
        dict4.setParamCode("diffArea_diffIndustry");
        dict4.setParamValue(String.valueOf(0.1));
        dict4.setVersion(1.0);
        dict4.setParamDesc("不同地区、不同行业");
        dict4.setParentId(dict.getId());
        List<SysDictionary> list = new ArrayList<>();
        list.add(dict1);
        list.add(dict2);
        list.add(dict3);
        list.add(dict4);
        sysDictionaryService.saveDicts(list);
    }

    @Test
    public void getCount(){
        System.out.println(sysDictionaryService.getCount());
    }


}