package com.unitedratings.lhcrm.service.interfaces;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.entity.LargeAmountSettings;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-10-30 下午3:39
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class LargeAmountSettingsServiceTest {
    @Autowired
    private LargeAmountSettingsService largeAmountSettingsService;

    @Test
    public void addSettings() throws IOException, InvalidFormatException {
        Matrix matrix = readSettingsMatrixFromExcel();
        LargeAmountSettings largeAmountSettings = new LargeAmountSettings();
        largeAmountSettings.setCreateTime(new Date());
        largeAmountSettings.setVersion(1.0);
        largeAmountSettings.setSettingsDetail(JSON.toJSONString(matrix.allValues()));
        largeAmountSettingsService.saveSettings(largeAmountSettings);
    }

    @Test
    public void getSettings() throws IOException, InvalidFormatException {
        LargeAmountSettings largeAmountSettings = largeAmountSettingsService.getNewestSettings();
        if(largeAmountSettings!=null){
            List<Double> doubles = JSON.parseArray(largeAmountSettings.getSettingsDetail(), Double.class);
            Matrix matrix = new DefaultDenseDoubleMatrix2D(19,19);
            int index = 0;
            for(long[] coo:matrix.allCoordinates()){
                matrix.setAsDouble(doubles.get(index++),coo);
            }
            System.out.println(matrix);
        }
    }

    private Matrix readSettingsMatrixFromExcel() throws InvalidFormatException, IOException {
        final OPCPackage pkg = OPCPackage.open("/Users/wangyongxin/Desktop/国兴大额测试终版.xlsx", PackageAccess.READ);
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        XSSFSheet settings = workbook.getSheet("Settings");
        int dataBeginCol = 6;
        int dataBeginRow = 1;
        Matrix matrix = new DefaultDenseDoubleMatrix2D(19,19);
        for(int r = dataBeginRow;r<20;r++){
            XSSFRow row = settings.getRow(r);
            if(row!=null){
                for (int c = dataBeginCol; c <= row.getLastCellNum(); c++) {
                    Cell cell = row.getCell(c);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                matrix.setAsBoolean(cell.getBooleanCellValue(), r-dataBeginRow, c-dataBeginCol);
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                matrix.setAsString(cell.getCellFormula(), r-dataBeginRow, c-dataBeginCol);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                matrix.setAsDouble(cell.getNumericCellValue(), r-dataBeginRow, c-dataBeginCol);
                                break;
                            case Cell.CELL_TYPE_STRING:
                                matrix.setAsString(cell.getStringCellValue(), r-dataBeginRow, c-dataBeginCol);
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
        }
        return matrix;
    }


}