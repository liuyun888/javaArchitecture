package com.demo.ftp.common;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: javaArchitecture
 * @description:
 * @author: LiuYunKai
 * @create: 2020-04-28 16:29
 **/
@Slf4j
public class FileUtil {
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(File file) {
        BufferedReader reader = null;
        List<String> strList = new ArrayList<>();
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                strList.add(tempString);
            }
            reader.close();

        } catch (IOException e) {
            log.error("文件读取时出现错误！");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    log.error("流关闭异常！");
                }
            }
        }
        return strList;
    }

    /**
     * 方法追加文件：使用FileWriter
     * @param fileName
     * @param content
     */
    public static void appendMethod(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除单个文件
     * @param file 要删除的文件对象
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 批量删除文件
     * @param files
     */
    public static void batchDeleteFile(List<File> files) {
        for (File file : files) {
            deleteFile(file);
        }
    }
}
