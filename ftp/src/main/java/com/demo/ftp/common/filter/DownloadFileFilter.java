package com.demo.ftp.common.filter;

import com.demo.ftp.common.FileUtil;
import com.demo.ftp.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: javaArchitecture
 * @description: Ftp文件过滤器
 * 将只下载当天，且下载目录中不存在的文件
 * @author: LiuYunKai
 * @create: 2020-04-28 16:17
 **/
@Slf4j
@Component
@PropertySource(value = { "/ftp.properties" })
public class DownloadFileFilter implements Constant {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    @Value("${ftp.local-files-prefix}")
    private String filePrefix;

    @Value("${ftp.local-files-suffix}")
    private String fileSuffix;

    /**
     * 过滤下载文件
     * @param file
     * @return true=下载，false=不下载
     */
    public boolean accept(FTPFile file) {
        long lastModified = file.getTimestamp().getTime().getTime();
        String fileName = file.getName();
        return isLatestFile(lastModified) && !isInLocalDir(fileName) ? true : false;
    }

    /**
     * 文件是否已在本地目录中
     * @param fileName
     * @return  false=不存在   true=已存在了
     */
    private boolean isInLocalDir(String fileName) {
        try {

            //获取本地文件夹中已下载的文件名
            File fileDir = new File(TMP_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            String path = TMP_DIR + filePrefix + simpleDateFormat.format(new Date()) + fileSuffix;
            File file = new File(path);

            if (!file.exists()) {//如果不存在就创建
                file.createNewFile();
                FileUtil.appendMethod(path, fileName + "\r\n");
                return false;
            }

            List<String> localFileNames = FileUtil.readFileByLines(file);
            if (localFileNames.contains(fileName)) {
                return true;
            } else {
                FileUtil.appendMethod(path, fileName + "\r\n");
                return false;
            }
        } catch (Exception e) {
            log.error("获取本地已下载文件列表出错", e);
            return true;
        }
    }
    /**
     * 文件是否为今天的数据
     * @param lastModified
     * @return true=是今天的文件 false=不是今天的文件
     */
    public boolean isLatestFile(long lastModified) {
        Date lastDate = new Date(lastModified);
        String lastDateStr = simpleDateFormat.format(lastDate);

        String todayStr = simpleDateFormat.format(new Date());
        return todayStr.equals(lastDateStr);
    }
}
