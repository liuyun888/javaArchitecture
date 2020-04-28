package com.demo.ftp.controller;

import com.demo.ftp.service.FtpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @program: javaArchitecture
 * @description: 测试 FTP
 * @author: LiuYunKai
 * @create: 2020-04-28 09:53
 **/
@RestController
public class TestFtpController {
    @Autowired
    private FtpClientService ftpClientService;

    @GetMapping("/testFtpUpload")
    public void test(@RequestParam("filePath") String filePath,@RequestParam("originName") String originName) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            ftpClientService.uploadFile(inputStream, originName, "/20200428/");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/testFtpDownload")
    public void testDownload(@RequestParam("remoteFileName") String remoteFileName,
                             @RequestParam("localFileName") String localFileName, @RequestParam("remoteDir") String remoteDir) {
        try {
            ftpClientService.download(remoteFileName, localFileName, remoteDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
