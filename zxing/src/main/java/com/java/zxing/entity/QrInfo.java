package com.java.zxing.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: javaArchitecture
 * @description: 二维码信息
 * @author: LiuYunKai
 * @create: 2020-05-09 15:23
 **/
@Data
@Accessors(chain = true)
public class QrInfo {

    private String id;


    private String json;


}
