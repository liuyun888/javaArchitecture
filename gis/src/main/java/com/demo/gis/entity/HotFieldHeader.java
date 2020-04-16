package com.demo.gis.entity;

import com.demo.gis.common.constant.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @program: ops-china
 * @description: 热力图+矢量图组合头部
 * @author: LiuYunKai
 * @create: 2020-04-02 09:52
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel
public class HotFieldHeader implements Constant {
    @ApiModelProperty(value = "")
    private Object discipline = 0;
    @ApiModelProperty(value = "")
    private Object disciplineName = "Meteorologicalproducts";
    @ApiModelProperty(value = "")
    private Object gribEdition = 2;
    @ApiModelProperty(value = "")
    private Object gribLength = 43435;
    @ApiModelProperty(value = "")
    private Object center = 7;
    @ApiModelProperty(value = "")
    private Object centerName = "USNationalWeatherService-NCEP(WMC)";
    @ApiModelProperty(value = "")
    private Integer subcenter = 0;
    @ApiModelProperty(value = "")
    private long significanceOfRT = 0;
    @ApiModelProperty(value = "")
    private String significanceOfRTName = "Analysis";
    @ApiModelProperty(value = "")
    private Integer productStatus = 0;
    @ApiModelProperty(value = "")
    private String productStatusName = "Operationalproducts";
    @ApiModelProperty(value = "")
    private Integer productType = 0;
    @ApiModelProperty(value = "")
    private Object productTypeName = "Analysisproducts";
    @ApiModelProperty(value = "")
    private Object productDefinitionTemplate = 0;
    @ApiModelProperty(value = "")
    private Object productDefinitionTemplateName = "Analysis/forecastathorizontallevel/layeratapointintime";
    @ApiModelProperty(value = "")
    private Object parameterCategory = 0;
    @ApiModelProperty(value = "")
    private Object parameterCategoryName = "Temperature";
    @ApiModelProperty(value = "")
    private Object parameterNumber = 0;
    @ApiModelProperty(value = "")
    private Object parameterNumberName = "Temperature";
    @ApiModelProperty(value = "")
    private Object parameterUnit = "K";
    @ApiModelProperty(value = "")
    private Object genProcessType = 0;
    @ApiModelProperty(value = "")
    private Object genProcessTypeName = "Analysis";
    @ApiModelProperty(value = "")
    private Object forecastTime = 0;
    @ApiModelProperty(value = "")
    private Object surface1Type = 100;
    @ApiModelProperty(value = "")
    private Object surface1TypeName = "Isobaricsurface";
    @ApiModelProperty(value = "")
    private Object surface1Value = 100000.0;
    @ApiModelProperty(value = "")
    private Object surface2Type = 255;
    @ApiModelProperty(value = "")
    private Object surface2TypeName = "Missing";
    @ApiModelProperty(value = "")
    private Object surface2Value = 0.0;
    @ApiModelProperty(value = "")
    private Object gridDefinitionTemplate = 0;
    @ApiModelProperty(value = "")
    private Object gridDefinitionTemplateName = "Latitude_Longitude";
    @ApiModelProperty(value = "")
    private Object shape = 6;
    @ApiModelProperty(value = "")
    private Object shapeName = "Earthsphericalwithradiusof6,371,229.0m";
    @ApiModelProperty(value = "")
    private Object gridUnits = "degrees";
    @ApiModelProperty(value = "")
    private Object resolution = 48;
    @ApiModelProperty(value = "")
    private Object winds = "true";
    @ApiModelProperty(value = "")
    private Object scanMode = 0;
    @ApiModelProperty(value = "")
    private Object basicAngle = 0;
    @ApiModelProperty(value = "")
    private Object subDivisions = 0;

    /**=============需要变化的值================**/
    @ApiModelProperty(value = "时间")
    private Object refTime;
    @ApiModelProperty(value = "点位数量")
    private Object numberPoints;
    @ApiModelProperty(value = "经度网格数")
    private Object nx;
    @ApiModelProperty(value = "纬度网格数")
    private Object ny;
    @ApiModelProperty(value = "开始经度，小的值开始")
    private Object lo1;
    @ApiModelProperty(value = "开始纬度，自北向南")
    private Object la1;
    @ApiModelProperty(value = "结束经度")
    private Object lo2;
    @ApiModelProperty(value = "结束纬度")
    private Object la2;
    @ApiModelProperty(value = "经度间隔值")
    private Object dx;
    @ApiModelProperty(value = "纬度间隔值")
    private Object dy;

    public static HotFieldHeader InitTyphoonInfoForEarth(float[] lon, float[] lat, long ts, int sub, int num, String name) {
        HotFieldHeader header = new HotFieldHeader();
        header.setLo1(lon[0]);
        header.setLa1(lat[0]);
        header.setLo2(lon[lon.length-1]);
        header.setLa2(lat[lat.length-1]);
        header.setDx(lon[sub] - lon[0]);
        header.setDy(lat[0] - lat[sub]);
        header.setNx(lon.length / sub + 1);
        header.setNy(lat.length / sub + 1);
        header.setNumberPoints((lon.length / sub + 1) * (lat.length / sub + 1));
        header.setRefTime(ts);
        header.setParameterNumber(num);
        header.setParameterNumberName(name);
        return header;
    }


    public static HotFieldHeader InitTyphoonInfoByLatForEarth(float[] lon, float[] lat, long ts, int sub, int num, String name) {
        HotFieldHeader header = new HotFieldHeader();
        header.setLo1(-130.0D);
        header.setLa1(90.0D);
        header.setLo2(230.0D);
        header.setLa2(-90.0D);
        header.setDx(lon[sub] - lon[0]);
        header.setDy(lat[0] - lat[sub]);
        header.setNx(lon.length / sub + 1);
        header.setNy(lat.length / sub + 1);
        header.setNumberPoints((lon.length / sub + 1) * (lat.length / sub + 1));
        header.setRefTime(ts);
        header.setParameterNumber(num);
        header.setParameterNumberName(name);
        return header;
    }

    public static HotFieldHeader InitTyphoonInfoByLatForEarthCurrent(float[] lon, float[] lat, long ts, int sub, int num, String name) {
        HotFieldHeader header = new HotFieldHeader();
        header.setLo1(105D);
        header.setLa1(16.0D);
        header.setLo2(130.0D);
        header.setLa2(41.0D);
        header.setDx(lon[sub] - lon[0]);
        header.setDy(lat[0] - lat[sub]);
        header.setNx(lon.length / sub + 1);
        header.setNy(lat.length / sub + 1);
        header.setNumberPoints((lon.length / sub + 1) * (lat.length / sub + 1));
        header.setRefTime(ts);
        header.setParameterNumber(num);
        header.setParameterNumberName(name);
        return header;
    }

}
