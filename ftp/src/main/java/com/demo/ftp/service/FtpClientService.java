package com.demo.ftp.service;

import java.io.InputStream;

/**
 * @program: javaArchitecture
 * @description:
 * @author: LiuYunKai
 * @create: 2020-04-28 09:49
 **/
public interface FtpClientService {
    String readFileToBase64(String remoteFileName, String remoteDir);

    void download(String remoteFileName, String localFileName, String remoteDir);

    boolean uploadFile(InputStream inputStream, String originName, String remoteDir);

    void downLoadTodayFile();

}
