package com.demo.gis.common.toolUtil;


import com.demo.gis.common.constant.Constant;


/**
 * @program: ops-china
 * @description: 矢量场NC文件解析工具类
 * @author: LiuYunKai
 * @create: 2020-03-10 10:52
 **/
public class FiledNcAnalyze implements Constant {

    //不需要调换纬度数据的用
    public static double[] JoinArrayForEarth(int lon, int lat, short[][][] dataArray, int sub, String name) {
        int latLength = lat / sub + 1;
        int lonLength = lon / sub + 1;
        int arrayLength = dataArray[0].length;

        //NC数据 lat在外，lon在内，不需要调换lat
        double[] arr = new double[latLength * lonLength];
        for (int i = 0, index2 = 0; i < arrayLength; i += sub, index2++) {
            //第i个经度数组
            short[] arr1 = dataArray[0][i];

            //清洗数据后的数组
            double[] arr2 = new double[lonLength-1];

            for (int j = 0, index = 0; j < arr1.length; j += sub, index++) {
                if (WIND.equals(name)) {
                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 0.0010621605509104192 + 1.1575595057707537;
                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;

                    arr2[index] = (val == -32767 ? 0 : val);
                }else if (AIR_TEM.equals(name)) {
                    arr2[index] = (double) ((arr1[j] == -32767.0 ? 0 : arr1[j]) + 273.15);
                } else if (PRES.equals(name)) {
                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 0.1983218378526849 + 99766.52583908108;
                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;
                    arr2[index] = (val == -32767 ? 0 : val);

                } else if (SALT.equals(name)) {
                    arr2[index] = (arr1[j] == 9999.0 ? 0 : arr1[j]);
                }
            }

            System.arraycopy(arr2, 0, arr, index2 * arr2.length, arr2.length);
        }
        return arr;
    }
}
