package com.demo.gis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;


/**
 * @program: ops-china
 * @description: 风场信息
 * @author: LiuYunKai
 * @create: 2020-03-06 14:40
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel
@Document(collection = "typhoon_wind_field")
public class TyphoonWindField {
    @Id
    private String id;

    @ApiModelProperty(value = "时间")
    private Long time;

    @ApiModelProperty(value = "JSON数据")
    private String json;

}
