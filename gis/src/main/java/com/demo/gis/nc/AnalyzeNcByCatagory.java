package com.demo.gis.nc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 根据类型解析NC
 * @author: LiuYunKai
 * @create: 2020-04-13 14:26
 **/
public class AnalyzeNcByCatagory {
    /**
     * 获取一个文件夹下的所有文件全路径
     *
     * @param path
     * @param listFileName
     */
    public static void getAllFileName(String path, ArrayList<String> listFileName) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                completNames[i] = path + names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
        for (File a : files) {
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                getAllFileName(a.getAbsolutePath() + "\\", listFileName);
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> listFileName = new ArrayList<String>();
        getAllFileName("D:\\GIS\\数据\\风场  -- NC\\ECwinds\\2018\\03", listFileName);
        for (String name : listFileName) {
            if (name.contains(".nc")) {
                System.out.println(name);
            }
        }
    }
}
