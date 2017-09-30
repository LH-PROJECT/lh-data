package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.IdealDefault;
import com.unitedratings.lhcrm.entity.IdealDefaultItem;
import com.unitedratings.lhcrm.entity.SysDictionary;
import com.unitedratings.lhcrm.utils.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ujmp.core.Matrix;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdealServiceSVTest {

    @Autowired
    private IdealServiceSV idealService;
    @Autowired
    private SysDictionaryServiceSV dictionaryService;

    @Test
    public void importIdealDefault() throws IOException, InvalidFormatException {
        File file = new File("/Users/wangyongxin/Desktop/模板.xlsx");
        final OPCPackage pkg = OPCPackage.open(file);
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        XSSFSheet sheet = workbook.getSheetAt(3);
        //获取理想违约率矩阵信息sheet有效行列数,模板中第0-6索引行，0、1索引列为表头信息，有效数据从第7索引行第2索引列开始
        final int dataBeginRow = 7;
        final int dataBeginCol = 2;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,true);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        //处理数据
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装资产相关系数矩阵信息矩阵
                cellDataProcess(dataBeginRow, dataBeginCol,validColSize, df, dateFormat, matrix, r, tempRow);
            }
        }

        int count = idealService.getIdealDefaultCount();
        if(count<1){
            List<SysDictionary> dictionaries = dictionaryService.getDictionaryListByParentId(2);
            IdealDefault idealDefault = new IdealDefault();
            idealDefault.setCreateTime(new Date());
            idealDefault.setVersion(1.0);
            idealService.saveIdealDefault(idealDefault);
            List<IdealDefaultItem> defaultItems = new ArrayList<>();
            for(int i = 0;i<matrix.getRowCount();i++){
                for(int j=0;j<matrix.getColumnCount();j++){
                    double rate = matrix.getAsDouble(i, j);
                    IdealDefaultItem idealDefaultItem = new IdealDefaultItem();
                    idealDefaultItem.setDefaultRate(rate);
                    idealDefaultItem.setLife(i+1);
                    idealDefaultItem.setIdealDefaultId(idealDefault.getId());
                    idealDefaultItem.setRankCode(dictionaries.get(j).getParamCode());
                    idealDefaultItem.setRankId(dictionaries.get(j).getId());
                    defaultItems.add(idealDefaultItem);
                }
            }
            idealService.saveIdealDefaultItemList(defaultItems);
        }
        pkg.close();
    }

    private static void cellDataProcess(int dataBeginRow, int dataBeginCol, int colSize, DecimalFormat df, DateFormat dateFormat, Matrix matrix, int r, Row row) {
        for (int c = dataBeginCol; c < colSize+dataBeginCol; c++) {
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
                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                            matrix.setAsString(dateFormat.format(cell.getDateCellValue()),r-dataBeginRow, c-dataBeginCol);
                        }else if(BuiltinFormats.getBuiltinFormat(0).equals(cell.getCellStyle().getDataFormatString())){
                            matrix.setAsString(df.format(cell.getNumericCellValue()),r-dataBeginRow, c-dataBeginCol);
                        }else {
                            matrix.setAsDouble(cell.getNumericCellValue(), r-dataBeginRow, c-dataBeginCol);
                        }
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