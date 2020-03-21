package com.demo.gis.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: ops-china
 * @description: 风场
 * @author: LiuYunKai
 * @create: 2020-03-09 09:23
 **/
public interface WindFieldService {
    /**
     * 解析风场NC文件
     * @return
     */
    boolean analyzeNCFile(MultipartFile file);

    boolean analyzeNCZIPFile(MultipartFile originalFile);
}
