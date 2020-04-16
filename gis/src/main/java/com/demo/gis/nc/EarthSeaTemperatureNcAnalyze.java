package com.demo.gis.nc;

import com.demo.gis.common.constant.Constant;
import com.demo.gis.common.toolUtil.CalendarUtils;
import com.demo.gis.common.toolUtil.FiledNcAnalyze;
import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.entity.HotFieldHeader;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @program: javaArchitecture
 * @description: 全球海温热力图
 * @author: LiuYunKai
 * @create: 2020-04-13 16:05
 **/
public class EarthSeaTemperatureNcAnalyze implements Constant {

    public static void main(String[] args) throws IOException, ParseException {
        NetcdfFile ncFile = NetcdfDataset.open("D:\\GIS\\数据\\海温 -- NC\\ecwmf_era5_temperature_grid0.25_areafull_date2020_01_1to2020_01_31.nc");
        // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
        String var1 = "longitude";
        String var2 = "latitude";
        String var3 = "time";
        String var5 = "sst";

        Variable v1 = ncFile.findVariable(var1);
        Variable v2 = ncFile.findVariable(var2);
        //time ---> :timezone
        Variable v3 = ncFile.findVariable(var3);
        Variable v5 = ncFile.findVariable(var5);

        float[] lon = (float[]) v1.read().copyToNDJavaArray();
        float[] lat = (float[]) v2.read().copyToNDJavaArray();

        int[] time = (int[]) v3.read().copyToNDJavaArray();

        //取得1970/1/1距离1900/1/1的小时数
        int hours = CalendarUtils.getHourNum();

        for (int i = 0; i < time.length; i++) {

            long ts = (time[i] - hours - 8) * 60 * 60;

            Date date = CalendarUtils.getDateByTimeStamp(ts);

            int sub = 4;

            int[] origin = new int[]{i, 0, 0};
            int[] size = new int[]{1, lat.length, lon.length};

            try {
                short[][][] sst = (short[][][]) v5.read(origin, size).copyToNDJavaArray();

                HotFieldHeader header = InitTyphoonInfo(lon, lat, ts, sub, 0, SEA_TEM);

                //开辟新数组长度为两数组之和
                double[] data = FiledNcAnalyze.JoinArrayForEarth(lon.length, lat.length, sst, sub, SEA_TEM);

                String jsonStr = InitHotJson(header, data);

                System.out.println(jsonStr);
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }
        }
    }

    public static HotFieldHeader InitTyphoonInfo(float[] lon, float[] lat, long ts, int sub, int num, String name) {
        HotFieldHeader header = new HotFieldHeader();
        header.setLo1(-100);
        header.setLa1(90.0);
        header.setLo2(259.5);
        header.setLa2(-90.0);
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

    //热力图的json
    public static String InitHotJson(HotFieldHeader header, double[] data) {
        List infoList = new ArrayList<>();
        Map<String, Object> UInfoMap = new HashMap<>(4);
        UInfoMap.put("header", header);
        UInfoMap.put("data", data);
        infoList.add(UInfoMap);

        Map<String, Object> result = new HashMap<>(1);
        result.put("dataJson", infoList);
        String jsonStr = JacksonUtils.toJSONString(result);

        return jsonStr;
    }
}
