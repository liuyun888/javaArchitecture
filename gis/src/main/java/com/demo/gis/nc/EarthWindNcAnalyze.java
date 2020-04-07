package com.demo.gis.nc;

import com.demo.gis.common.constant.Constant;
import com.demo.gis.common.toolUtil.CalendarUtils;
import com.demo.gis.common.toolUtil.FiledNcAnalyze;
import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.entity.HotFieldHeader;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * @program: javaArchitecture
 * @description: 全球风场
 * @author: LiuYunKai
 * @create: 2020-04-03 16:33
 **/
public class EarthWindNcAnalyze implements Constant {

    public static void main(String[] args) throws IOException, ParseException {
        NetcdfFile ncFile = NetcdfDataset.open("D:\\GIS\\数据\\风场  -- NC\\全球数据\\ecwmf_era5_u10v10_grid0.25_areafull_date2018_03_1to2018_03_31.nc");
        // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
        String var1 = "longitude";
        String var2 = "latitude";
        String var3 = "time";
        String var4 = "u10";
        String var5 = "v10";

        Variable v1 = ncFile.findVariable(var1);
        Variable v2 = ncFile.findVariable(var2);
        //time ---> :timezone
        Variable v3 = ncFile.findVariable(var3);
        //:missing_value  ---> Double
        Variable v4 = ncFile.findVariable(var4);
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
            short[][][] windU = (short[][][]) v4.read().copyToNDJavaArray();
            HotFieldHeader UType = HotFieldHeader.InitTyphoonInfoForDouble(lon, lat, ts, sub, 2, U_WIND);

            short[][][] windV = (short[][][]) v5.read().copyToNDJavaArray();
            HotFieldHeader VType = HotFieldHeader.InitTyphoonInfoForDouble(lon, lat, ts, sub, 3, V_WIND);

            //开辟新数组长度为两数组之和
            double[] UArr = FiledNcAnalyze.JoinArrayForEarthWind(lon.length, lat.length, windU,sub);
            double[] VArr = FiledNcAnalyze.JoinArrayForEarthWind(lon.length, lat.length, windV,sub);


            String json = InitJson(UType, VType, UArr, VArr, date);

            System.out.println(json);
        }
    }

    public static String InitJson(HotFieldHeader UType, HotFieldHeader VType, double[] UArr, double[] VArr, Date date) {
        List infoList = new ArrayList<>();
        Map<String, Object> UInfoMap = new HashMap<>(4);
        UInfoMap.put("header", UType);
        UInfoMap.put("data", UArr);
        UInfoMap.put("meta", date);
        infoList.add(UInfoMap);

        Map<String, Object> VInfoMap = new HashMap<>(4);
        VInfoMap.put("header", VType);
        VInfoMap.put("data", VArr);
        VInfoMap.put("meta", date);
        infoList.add(VInfoMap);

        Map<String, Object> data = new HashMap<>(1);
        data.put("dataJson", infoList);
        String jsonStr = JacksonUtils.toJSONString(data);

        return jsonStr;
    }
}
