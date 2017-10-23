package com.unitedratings.lhcrm.utils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDate;

/**
 * @author wangyongxin
 */
public class FileUtil {

    /**
     * 按年月创建目录
     * @return
     */
    public static String createChildPath(String dir){
        LocalDate now = LocalDate.now();
        String childPath = now.getYear() + File.separator + now.getMonthValue();
        File childDir = new File(dir+File.separator+childPath);
        if(!childDir.exists()){
            childDir.mkdirs();
        }
        return childPath;
    }

    /**
     * 从路径中抽取文件名
     * @param resultFilePath
     * @return
     */
    public static String extractFileName(String resultFilePath) {
        String fileName = "";
        if(!StringUtils.isEmpty(resultFilePath)){
            fileName = resultFilePath.substring(resultFilePath.lastIndexOf('_')+1);
        }
        return fileName;
    }
}
