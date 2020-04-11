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
 * @description: 全球海浪热力图
 * @author: LiuYunKai
 * @create: 2020-04-11 11:34
 **/
public class EarthWaveHotNcAnalyze implements Constant {


    public static void main(String[] args) throws IOException, ParseException {
        NetcdfFile ncFile = NetcdfDataset.open("D:\\GIS\\数据\\海浪-NC\\全球\\ecwmf_era5_wave_grid0.5_areafull_date2018_03_1to2018_03_31.nc");
        // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
        String var1 = "longitude";
        String var2 = "latitude";
        String var3 = "time";
        String var5 = "swh";

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

            int sub = 40;

            int[] origin = new int[]{i, 0, 0};
            int[] size = new int[]{1, lat.length, lon.length};

            try {
                short[][][] swh = (short[][][]) v5.read(origin, size).copyToNDJavaArray();

                HotFieldHeader header = HotFieldHeader.InitTyphoonInfoByLatForEarth(lon, lat, ts, sub, 0, WAVE);

                //开辟新数组长度为两数组之和
                double[] data = FiledNcAnalyze.JoinArrayForEarth(lon.length, lat.length, swh, sub, WAVE);

                String jsonStr = InitHotJson(header, data);

                System.out.println(jsonStr);
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }
        }
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
