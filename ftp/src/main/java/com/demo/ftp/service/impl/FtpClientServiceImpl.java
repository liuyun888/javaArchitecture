package com.demo.ftp.service.impl;

import com.demo.ftp.service.FtpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @program: javaArchitecture
 * @description:
 * @author: LiuYunKai
 * @create: 2020-04-28 09:50
 **/
@Slf4j
@Service
public class FtpClientServiceImpl implements FtpClientService {


    @Autowired
    private FTPClient ftpClient;

    @Override
    public String readFileToBase64(String remoteFileName,String remoteDir) {
        if (ftpClient == null){
            return null;
        }

        String base64 = "";
        InputStream inputStream = null;

        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
            Boolean flag = false;
            //遍历当前目录下的文件，判断要读取的文件是否在当前目录下
            for (FTPFile ftpFile:ftpFiles){
                if (ftpFile.getName().equals(remoteFileName)){
                    flag = true;
                }
            }

            if (!flag){
                log.error("directory：{}下没有 {}",remoteDir,remoteFileName);
                return null;
            }
            //获取待读文件输入流
            inputStream = ftpClient.retrieveFileStream(remoteDir+remoteFileName);

            //inputStream.available() 获取返回在不阻塞的情况下能读取的字节数，正常情况是文件的大小
            byte[] bytes = new byte[inputStream.available()];

            inputStream.read(bytes);//将文件数据读到字节数组中
            BASE64Encoder base64Encoder = new BASE64Encoder();
            base64 = base64Encoder.encode(bytes);//将字节数组转成base64字符串
            log.info("read file {} success",remoteFileName);
            ftpClient.logout();
        } catch (IOException e) {
            log.error("read file fail ----->>>{}",e.getCause());
            return null;
        }finally {
            if (ftpClient.isConnected()){
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}",e.getCause());
                }
            }

            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close fail -------- {}",e.getCause());
                }
            }

        }

        return base64;
    }

    @Override
    public void download(String remoteFileName, String localFileName,String remoteDir) {
        if (ftpClient == null){
            return;
        }

        OutputStream outputStream = null;

        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
            Boolean flag = false;
            //遍历当前目录下的文件，判断是否存在待下载的文件
            for (FTPFile ftpFile:ftpFiles){
                if (ftpFile.getName().equals(remoteFileName)){
                    flag = true;
                    break;
                }
            }

            if (!flag){
                log.error("directory：{}下没有 {}",remoteDir,remoteFileName);
                return ;
            }

            String localDir = "D:\\";
            outputStream = new FileOutputStream(localDir+localFileName);//创建文件输出流

            Boolean isSuccess = ftpClient.retrieveFile(remoteFileName,outputStream); //下载文件
            if (!isSuccess){
                log.error("download file 【{}】 fail",remoteFileName);
            }

            log.info("download file success");
            ftpClient.logout();
        } catch (IOException e) {
            log.error("download file 【{}】 fail ------->>>{}",remoteFileName,e.getCause());
        }finally {
            if (ftpClient.isConnected()){
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}",e.getCause());
                }
            }

            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close fail ------->>>{}",e.getCause());
                }
            }
        }
    }

    /**
     *  上传文件
     * @param inputStream 待上传文件的输入流
     * @param originName 文件保存时的名字
     * @param remoteDir 文件要存放的目录
     */
    @Override
    public boolean uploadFile(InputStream inputStream, String originName, String remoteDir){
        if (ftpClient == null){
            return false;
        }

        try {
            ftpClient.changeWorkingDirectory(remoteDir);//进入到文件保存的目录
            Boolean isSuccess = ftpClient.storeFile(originName,inputStream);//保存文件
//            if (!isSuccess){
//                throw new BusinessException(ResponseCode.UPLOAD_FILE_FAIL_CODE,originName+"---》上传失败！");
//            }
            log.info("{}---》上传成功！",originName);
            ftpClient.logout();
            return true;
        } catch (IOException e) {
            log.error("{}---》上传失败！",originName);
//            throw new BusinessException(ResponseCode.UPLOAD_FILE_FAIL_CODE,originName+"上传失败！");
            return false;
        }finally {
            if (ftpClient.isConnected()){
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}",e.getCause());
                }
            }
        }
    }
}
