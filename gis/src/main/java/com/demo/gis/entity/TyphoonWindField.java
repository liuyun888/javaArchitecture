package com.demo.gis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @program: ops-china
 * @description: 风场信息
 * @author: LiuYunKai
 * @create: 2020-03-06 14:40
 **/
@Data
@Accessors(chain = true)
@ApiModel
@Document(collection = "typhoon_wind_field")
public class TyphoonWindField {
    @Id
    private String id;
    @ApiModelProperty(value = "开始经度，小的值开始")
    private Float lo1;
    @ApiModelProperty(value = "开始纬度，大的值开始")
    private Float la1;
    @ApiModelProperty(value = "结束经度")
    private Float lo2;
    @ApiModelProperty(value = "结束纬度")
    private Float la2;
    @ApiModelProperty(value = "经度间隔值")
    private Float dx;
    @ApiModelProperty(value = "纬度间隔值")
    private Float dy;
    @ApiModelProperty(value = "经度网格数")
    private Integer nx;
    @ApiModelProperty(value = "纬度网格数")
    private Integer ny;
    @ApiModelProperty(value = "时间")
    private String refTime;
    @ApiModelProperty(value = "单位")
    private String parameterUnit;
    @ApiModelProperty(value = "标识")
    private Integer parameterNumber;
    @ApiModelProperty(value = "标识名")
    private String parameterNumberName;
    @ApiModelProperty(value = "标识分类")
    private Integer parameterCategory;

}
