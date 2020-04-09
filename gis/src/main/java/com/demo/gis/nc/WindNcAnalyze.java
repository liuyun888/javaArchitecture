package com.demo.gis.nc;

import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.vo.TyphoonWindFieldVo;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            NetcdfFile ncFile  = NetcdfDataset.open("D:\\GIS\\数据\\风场  -- NC\\ECwinds\\2018\\03\\RA.gfs.wind.2018030102" +
                    ".nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            String var3 = "time";
            String var4 = "windu";
            String var5 = "windv";


            Variable v1 = ncFile.findVariable(var1);
            Variable v2 = ncFile.findVariable(var2);
            //time ---> :timezone
            Variable v3 = ncFile.findVariable(var3);
            //:missing_value  ---> Double
            Variable v4 = ncFile.findVariable(var4);
            Variable v5 = ncFile.findVariable(var5);

            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
            // 二维、三维一样原理

            DataType type = v1.getDataType();

            if(type.name().equals("FLOAT")){
                float[] lon = (float[]) v1.read().copyToNDJavaArray();
                float[] lat = (float[]) v2.read().copyToNDJavaArray();

                int[] time = (int[]) v3.read().copyToNDJavaArray();
                float[][][] windu = (float[][][]) v4.read().copyToNDJavaArray();
                float[][][] windv = (float[][][]) v5.read().copyToNDJavaArray();

                int[] origin = new int[] { 0, 1, 1 };
                int[] size = new int[] { 1, 50, 50 };
                try {
                    float[][][] windu2 = (float[][][]) v4.read(origin,size).copyToNDJavaArray();
                    System.out.println("windu2.length :" + windu2.length);
                    System.out.println("windu2[0].length :" + windu2[0].length);
//
                    System.out.println("windu2[0][0].length :" + windu2[0][0].length);
                    System.out.println("windu2[0][0]:" + Arrays.toString(windu2[0][0]));
                    Array array =  v4.read(origin,size);
//                    System.out.println("读取从第一维的0开始,第二维从1开始,第三维从1开始,数量分别为2,2,2：\n" + NCdumpW.printArray(array));

                } catch (InvalidRangeException e) {
                    e.printStackTrace();
                }


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
                TyphoonWindFieldVo UType = new TyphoonWindFieldVo();
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


                TyphoonWindFieldVo VType = new TyphoonWindFieldVo();
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
            }else if(type.name().equals("DOUBLE")){

                double[] lon = (double[]) v1.read().copyToNDJavaArray();
                double[] lat = (double[]) v2.read().copyToNDJavaArray();
                double[] time = (double[]) v3.read().copyToNDJavaArray();
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
                TyphoonWindFieldVo UType = new TyphoonWindFieldVo();
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


                TyphoonWindFieldVo VType = new TyphoonWindFieldVo();
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
                float[] UArr = joinArrayForDouble(lon, lat, windu);
                float[] VArr = joinArrayForDouble(lon, lat, windv);


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


            }



        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private static float[] joinArray(float[] lon, float[] lat, float[][][] wind) {

        //NC数据 lat在外，lon在内，需要调换lat
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < wind[0].length; i++) {

            float[] arr1 = wind[0][wind[0].length - 1 - i];

//            float[] arr2 = reverseArray(arr1);

            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }

        return arr;
    }

    private static float[] joinArrayForDouble(double[] lon, double[] lat, float[][][] wind) {

        //NC数据 lat在外，lon在内，需要调换lat
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < wind[0].length; i++) {

            float[] arr1 = wind[0][wind[0].length - 1 - i];

            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }

        return arr;
    }
    private static float[] reverseArray(float[] Array) {
        float[] new_array = new float[Array.length];
        for (int i = 0; i < Array.length; i++) {
            // 反转后数组的第一个元素等于源数组的最后一个元素：
            new_array[i] = Array[Array.length - i - 1];
        }
        return new_array;

    }


}
