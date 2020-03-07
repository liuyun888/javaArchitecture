package com.demo.gis.nc;
import java.util.*;


import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.entity.TyphoonWindField;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @program: javaArchitecture
 * @description: 风场NC文件分析
 * @author: LiuYunKai
 * @create: 2020-02-26 14:53
 **/
public class WindNcAnalyze {

    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\风场  -- NC\\RA.gfs.wind.2020011508.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            String var3 = "time";
            String var4 = "windu";
            String var5 = "windv";


            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            //time ---> :timezone
            Variable v3 = ncfile.findVariable(var3);
            //:missing_value  ---> Double
            Variable v4 = ncfile.findVariable(var4);
            Variable v5 = ncfile.findVariable(var5);

            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
            // 二维、三维一样原理
            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

            int[] time = (int[]) v3.read().copyToNDJavaArray();
            float[][][] windu = (float[][][]) v4.read().copyToNDJavaArray();
            float[][][] windv = (float[][][]) v5.read().copyToNDJavaArray();

            System.out.println("风场NC文件分析");
            System.out.println("lon :" + Arrays.toString(lon));
            System.out.println("lon.length :" + lon.length);
            System.out.println("=====================================");
            System.out.println("lat :" + Arrays.toString(lat));
            System.out.println("lat.length :" + lat.length);
            System.out.println("=====================================");
            System.out.println("time :" + Arrays.toString(time));
            System.out.println("time.length :" + time.length);
            System.out.println("=====================================");
            System.out.println("windu :" + Arrays.toString(windu));
            System.out.println("windu.length :" + windu.length);
            System.out.println("windu[0].length :" + windu[0].length);
//
            System.out.println("windu[0][0].length :" + windu[0][0].length);
            System.out.println("windu[0][0]:" + Arrays.toString(windu[0][0]));


            //拼接成leaflet支持的JSON -- U分量
            //页面展示经度从左往右，纬度从上往下，所以需要针对纬度数据做翻转
            TyphoonWindField UType = new TyphoonWindField();
            UType.setLo1(lon[0]);
            UType.setLa2(lat[0]);
            UType.setLo2(lon[lon.length - 1]);
            UType.setLa1(lat[lat.length - 1]);
            UType.setDx(lon[1] - lon[0]);
            UType.setDy(lat[1] - lat[0]);
            UType.setNx(lon.length);
            UType.setNy(lat.length);
            String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time[0]);
            UType.setRefTime(timeStr);
            UType.setParameterUnit("m/s");
            UType.setParameterNumber(2);
            UType.setParameterNumberName("eastward_wind");
            UType.setParameterCategory(2);


            TyphoonWindField VType = new TyphoonWindField();
            VType.setLo1(lon[0]);
            VType.setLa2(lat[0]);
            VType.setLo2(lon[lon.length - 1]);
            VType.setLa1(lat[lat.length - 1]);
            VType.setDx(lon[1] - lon[0]);
            VType.setDy(lat[1] - lat[0]);
            VType.setNx(lon.length);
            VType.setNy(lat.length);
            VType.setRefTime(timeStr);
            VType.setParameterUnit("m/s");
            VType.setParameterNumber(3);
            VType.setParameterNumberName("eastward_wind");
            VType.setParameterCategory(2);

            //开辟新数组长度为两数组之和
            float[] UArr = joinArray(lon, lat, windu);
            float[] VArr = joinArray(lon, lat, windv);


            List infoList = new ArrayList<>();
            Map<String,Object> UInfoMap = new HashMap<>(4);
            UInfoMap.put("header",UType);
            UInfoMap.put("data",UArr);
            infoList.add(UInfoMap);

            Map<String,Object> VInfoMap = new HashMap<>(4);
            VInfoMap.put("header",VType);
            VInfoMap.put("data",VArr);
            infoList.add(VInfoMap);

            Map<String, Object> data = new HashMap<>(1);
            data.put("dataJson", infoList);
            String json = JacksonUtils.toJSONString(data);

            System.out.println(json);


        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private static float[] joinArray(float[] lon, float[] lat, float[][][] wind) {
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < wind[0].length; i++) {
            System.arraycopy(wind[0][wind[0].length- 1 - i], 0, arr, i * wind[0][i].length, wind[0][i].length);
        }

        return arr;
    }


}
