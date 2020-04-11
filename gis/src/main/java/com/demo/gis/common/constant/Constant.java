package com.demo.gis.common.constant;

import java.io.File;

/**
 * @program: ops-china
 * @description: 常量
 * @author: LiuYunKai
 * @create: 2020-03-05 12:05
 **/
public interface Constant {
    String TMP_DIR = System.getProperty("java.io.tmpdir") + "NcFile" + File.separator;
    String WATER_DEPTH_PATH =  "E:\\workspace\\GIS\\数据\\水深 -- sbn\\水深点\\50.shp";

    String LON = "lon";
    String LAT = "lat";
    String TIME = "time";
    String WIND_U = "windu";
    String WIND_V = "windv";

    String FLOAT = "FLOAT";
    String DOUBLE = "DOUBLE";

    String SYMBOL_DOT = ".";

    String UNITS = "m/s";
    String EAST_WIND = "eastward_wind";

    String WIND_INFO = "wind_info";
    String WAVE_INFO = "wave_info";
    String CURRENT_INFO = "current_info";
    String SEA_RISK = "sea_risk";

    String U_WIND = "U-component_of_wind";
    String V_WIND = "V-component_of_wind";

    String PRES = "Pres";
    String AIR_TEM = "Temperature";
    String SALT = "Salt";
    String WIND = "Wind";
    String WAVE = "Wave";

    String U_WAVE = "U-component_of_wave";
    String V_WAVE = "V-component_of_wave";
}
