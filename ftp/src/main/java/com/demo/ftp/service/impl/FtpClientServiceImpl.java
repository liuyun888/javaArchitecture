package com.demo.ftp.service.impl;

import com.demo.ftp.common.constant.Constant;
import com.demo.ftp.common.filter.DownloadFileFilter;
import com.demo.ftp.entities.ApplicationEntity;
import com.demo.ftp.service.FtpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: javaArchitecture
 * @description:
 * @author: LiuYunKai
 * @create: 2020-04-28 09:50
 **/
@Slf4j
@Service
public class FtpClientServiceImpl implements FtpClientService, Constant {

    @Autowired
    private DownloadFileFilter downloadFileFilter;

    @Autowired
    private ApplicationEntity applicationEntity;

    @Override
    public String readFileToBase64(String remoteFileName, String remoteDir) {
        FTPClient ftpClient = initFtpClient();

        String base64 = "";
        InputStream inputStream = null;

        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
            Boolean flag = false;
            //遍历当前目录下的文件，判断要读取的文件是否在当前目录下
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(remoteFileName)) {
                    flag = true;
                }
            }

            if (!flag) {
                log.error("directory：{}下没有 {}", remoteDir, remoteFileName);
                return null;
            }
            //获取待读文件输入流
            inputStream = ftpClient.retrieveFileStream(remoteDir + remoteFileName);

            //inputStream.available() 获取返回在不阻塞的情况下能读取的字节数，正常情况是文件的大小
            byte[] bytes = new byte[inputStream.available()];

            inputStream.read(bytes);//将文件数据读到字节数组中
            BASE64Encoder base64Encoder = new BASE64Encoder();
            base64 = base64Encoder.encode(bytes);//将字节数组转成base64字符串
            log.info("read file {} success", remoteFileName);
            ftpClient.logout();
        } catch (IOException e) {
            log.error("read file fail ----->>>{}", e.getCause());
            return null;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}", e.getCause());
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close fail -------- {}", e.getCause());
                }
            }

        }

        return base64;
    }

    @Override
    public void download(String remoteFileName, String localFileName, String remoteDir) {
        FTPClient ftpClient = initFtpClient();
        OutputStream outputStream = null;

        try {
            ftpClient.changeWorkingDirectory(remoteDir);
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
            Boolean flag = false;
            //遍历当前目录下的文件，判断是否存在待下载的文件
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(remoteFileName)) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                log.error("directory：{}下没有 {}", remoteDir, remoteFileName);
                return;
            }

            String localDir = TMP_DIR;
            outputStream = new FileOutputStream(localDir + localFileName);//创建文件输出流

            Boolean isSuccess = ftpClient.retrieveFile(remoteFileName, outputStream); //下载文件
            if (!isSuccess) {
                log.error("download file 【{}】 fail", remoteFileName);
            }

            log.info("download file success");
//            ftpClient.logout();
        } catch (IOException e) {
            log.error("download file 【{}】 fail ------->>>{}", remoteFileName, e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}", e.getCause());
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close fail ------->>>{}", e.getCause());
                }
            }
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream 待上传文件的输入流
     * @param originName  文件保存时的名字
     * @param remoteDir   文件要存放的目录
     */
    @Override
    public boolean uploadFile(InputStream inputStream, String originName, String remoteDir) {
        FTPClient ftpClient = initFtpClient();
        try {
            ftpClient.changeWorkingDirectory(remoteDir);//进入到文件保存的目录
            Boolean isSuccess = ftpClient.storeFile(originName, inputStream);//保存文件
//            if (!isSuccess){
//                throw new BusinessException(ResponseCode.UPLOAD_FILE_FAIL_CODE,originName+"---》上传失败！");
//            }
            log.info("{}---》上传成功！", originName);
//            ftpClient.logout();
            return true;
        } catch (IOException e) {
            log.error("{}---》上传失败！{}", originName,e.getMessage());
//            throw new BusinessException(ResponseCode.UPLOAD_FILE_FAIL_CODE,originName+"上传失败！");
            return false;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}", e.getCause());
                }
            }
        }
    }

    @Override
    public void downLoadTodayFile() {
        FTPClient ftpClient = initFtpClient();

        OutputStream outputStream = null;
        String remoteFileName = "";
        try {
            ftpClient.changeWorkingDirectory(REMOTE_DIR);
            //FTP上有哪些文件
            FTPFile[] ftpFiles = ftpClient.listFiles(REMOTE_DIR);
            log.info("ftpFiles.length :【{}】  ------->>>{}", ftpFiles.length);
            List<String> remoteFileNames = new ArrayList<>(ftpFiles.length);
            boolean flag = false;
            //遍历当前目录下的文件，判断是否存在待下载的文件
            for (FTPFile ftpFile : ftpFiles) {
                if (downloadFileFilter.accept(ftpFile)) {
                    remoteFileNames.add(ftpFile.getName());
                    flag = true;
                }
            }

            if (!flag) {
                log.error("directory：{}下没有待下载文件", REMOTE_DIR);
                return;
            }

            for (int i = 0; i < remoteFileNames.size(); i++) {
                remoteFileName = remoteFileNames.get(i);

                String localDir = TMP_DIR;
                //创建文件输出流
                outputStream = new FileOutputStream(localDir + remoteFileName);
                //下载文件
                boolean isSuccess = ftpClient.retrieveFile(remoteFileName, outputStream);
                if (!isSuccess) {
                    log.error("download file 【{}】 fail", remoteFileName);
                }

                log.info("download file 【{}】 success ------->>>{}", remoteFileName);
            }

            ftpClient.logout();

        } catch (IOException e) {
            log.error("download file 【{}】 fail ------->>>{}", remoteFileName, e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    log.error("disconnect fail ------->>>{}", e.getCause());
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close fail ------->>>{}", e.getCause());
                }
            }
        }
    }

    public FTPClient initFtpClient() {
        FTPClient ftpClients = new FTPClient();
//        ftpClients.setConnectTimeout(1000 * 30);//设置连接超时时间
        ftpClients.setControlEncoding("utf-8");//设置ftp字符集
//        ftpClients.enterLocalPassiveMode();//设置被动模式，文件传输端口设置
        try {
//            ftpClients.setFileType(FTP.BINARY_FILE_TYPE);//设置文件传输模式为二进制，可以保证传输的内容不会被改变
//            ftpClients.connect(beansEntity.getIp()+":"+beansEntity.getPort());
            ftpClients.setDefaultPort(applicationEntity.getFtpPort());
            ftpClients.connect(applicationEntity.getFtpHost(), applicationEntity.getFtpPort());
            ftpClients.login(applicationEntity.getFtpUsername(), applicationEntity.getFtpPassword());
            int replyCode = ftpClients.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClients.disconnect();
                log.error("未连接到FTP，用户名或密码错误!");
                return null;
            } else {
                log.info("FTP连接成功!");
                return ftpClients;
            }
        } catch (SocketException socketException) {
            log.error("FTP的IP地址可能错误，请正确配置!");
            return null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            log.error("FTP的端口错误,请正确配置!");
            return null;
        }
    }

}
