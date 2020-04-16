package com.java.spring.boot.oss.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: javaArchitecture
 * @description: 配置文件配置项
 * @author: LiuYunKai
 * @create: 2020-04-14 14:33
 **/

@Component
public class ConstantProperties implements InitializingBean {
    @Value("${java4all.file.endpoint}")
    private String java4all_file_endpoint;

    @Value("${java4all.file.keyid}")
    private String java4all_file_keyid;

    @Value("${java4all.file.keysecret}")
    private String java4all_file_keysecret;

    @Value("${java4all.file.filehost}")
    private String java4all_file_filehost;

    @Value("${java4all.file.bucketname1}")
    private String java4all_file_bucketname1;


    public static String JAVA4ALL_END_POINT         ;
    public static String JAVA4ALL_ACCESS_KEY_ID     ;
    public static String JAVA4ALL_ACCESS_KEY_SECRET ;
    public static String JAVA4ALL_BUCKET_NAME1      ;
    public static String JAVA4ALL_FILE_HOST         ;

    @Override
    public void afterPropertiesSet() throws Exception {
        JAVA4ALL_END_POINT = java4all_file_endpoint;
        JAVA4ALL_ACCESS_KEY_ID = java4all_file_keyid;
        JAVA4ALL_ACCESS_KEY_SECRET = java4all_file_keysecret;
        JAVA4ALL_FILE_HOST = java4all_file_filehost;
        JAVA4ALL_BUCKET_NAME1 = java4all_file_bucketname1;
    }
}
