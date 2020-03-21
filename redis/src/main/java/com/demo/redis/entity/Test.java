package com.demo.redis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 16:44
 **/
@Data
@Accessors(chain = true)
@ApiModel
@Document(collection = "test")
public class Test {
    @Id
    private String id;

    @ApiModelProperty(value = "JSON数据")
    private String json;
}
