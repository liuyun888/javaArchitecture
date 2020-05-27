package com.java.zxing.controller;

import com.google.zxing.WriterException;
import com.java.zxing.utils.QrCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @program: javaArchitecture
 * @description: 二维码前端控制器
 * @author: LiuYunKai
 * @create: 2020-05-09 14:19
 **/
@RestController
public class QrCodeController {

    /**
     * 二维码
     * @param request
     * @param response
     */
    @RequestMapping("/qrcode")
    public void qrcode(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer url = request.getRequestURL();
        // 域名
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();

        // 再加上请求链接
        String requestUrl = tempContextUrl + "/index";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtils.encode(requestUrl, "/static/images/logo.png", os, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * test
     * @param request
     * @param response
     */
    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        //二维码信息内容
        String content = "this is test info";
        //二维码输出目录
        String path = "D:\\录屏\\头像.jpg";
        try {
            QrCodeUtils.createQrCode(new FileOutputStream(new File(path)), content, 90, "JPEG");
            String info = QrCodeUtils.readQrCode(new FileInputStream(new File(path)));
            System.out.println(info);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
