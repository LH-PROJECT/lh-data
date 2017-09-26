package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.Industry;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndustryServiceSVTest {

    @Autowired
    private IndustryServiceSV industryService;

    /**
     * 初始化导入行业信息
     * @throws InvalidFormatException
     * @throws IOException
     */
    @Test
    public void importIndustry() throws InvalidFormatException, IOException {
        File file = new File("/Users/wangyongxin/Downloads/WIND行业分类标准文件.xls");
        final FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
        final HSSFWorkbook workbook = new HSSFWorkbook(bufferedInputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        DecimalFormat df = new DecimalFormat("0");
        for(int c=0;c<4;c++){
            List<Industry> industries = new ArrayList<>();
            for(int r = 1;r<sheet.getLastRowNum();r++){
                Row row = sheet.getRow(r);
                if (row != null) {
                    Cell cell = row.getCell(2*c);
                    if(cell!=null){
                        Industry industry = new Industry();
                        String value = "";
                        if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                            value = cell.getStringCellValue();
                        }else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
                            value = df.format(cell.getNumericCellValue());
                        }
                        if(!StringUtils.isEmpty(value)){
                            industry.setIndustryCode(value);
                            if(c>0){
                                List<Industry> industryList = industryService.getIndustryLikeCode(value.substring(0,2*c));
                                if(!CollectionUtils.isEmpty(industryList)){
                                    Iterator<Industry> iterator = industryList.iterator();
                                    while (iterator.hasNext()){
                                        Industry next = iterator.next();
                                        if(value.startsWith(next.getIndustryCode())){
                                            industry.setParentIndustryCode(next.getIndustryCode());
                                        }
                                    }
                                }
                            }
                            cell = row.getCell(2*c + 1);
                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                value = cell.getStringCellValue();
                            }
                            industry.setIndustryName(value);
                            if(c==3){
                                cell = row.getCell(2*c + 2);
                                if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                    value = cell.getStringCellValue();
                                }
                                industry.setIndustryDesc(value);
                            }
                            industry.setCreateTime(new Date());
                            industry.setValid(true);
                            industry.setVersion(1.0);
                            industries.add(industry);
                        }
                    }
                }
            }
            industryService.saveIndustryList(industries);
        }
        bufferedInputStream.close();
    }

}