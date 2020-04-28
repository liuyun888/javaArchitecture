package com.demo.ftp.service;

import java.io.InputStream;

/**
 * @program: javaArchitecture
 * @description:
 * @author: LiuYunKai
 * @create: 2020-04-28 09:49
 **/
public interface FtpClientService {
    public String readFileToBase64(String remoteFileName, String remoteDir);

    public void download(String remoteFileName, String localFileName, String remoteDir);

    public boolean uploadFile(InputStream inputStream, String originName, String remoteDir);
}
