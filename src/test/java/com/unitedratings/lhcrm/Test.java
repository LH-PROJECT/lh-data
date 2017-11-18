package com.unitedratings.lhcrm;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Test {

    public static void main(String[] args) {
        for (int i=0;i<3;i++){
            new Thread(()->{
                Random random = new Random();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Thread.currentThread().getId()+":");
                for(int j=0;j<10;j++){
                    stringBuilder.append(random.nextGaussian()+"\t");
                }
                stringBuilder.append("\n");
                System.out.println(stringBuilder);
            }).start();
        }
    }

    @org.junit.Test
    public void test() throws IOException, InvalidFormatException {
        List<Matrix> matrices = AssetsExcelProcess.processRandomSheet();
        System.out.println(matrices);
    }

    @org.junit.Test
    public void test1() {
        Matrix matrix = new DefaultDenseDoubleMatrix2D(3,3);
        int index = 0;
        for(long[] coo:matrix.allCoordinates()){
            matrix.setAsDouble(index,coo);
            index++;
        }
        System.out.println(matrix);
        System.out.println(JSON.toJSONString(matrix.allValues()));
    }
}
