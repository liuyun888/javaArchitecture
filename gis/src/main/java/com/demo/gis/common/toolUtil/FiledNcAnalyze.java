package com.demo.gis.common.toolUtil;


import com.demo.gis.common.constant.Constant;


/**
 * @program: ops-china
 * @description: 矢量场NC文件解析工具类
 * @author: LiuYunKai
 * @create: 2020-03-10 10:52
 **/
public class FiledNcAnalyze implements Constant {

    public static double[] JoinArrayForEarthWind(int lon, int lat, short[][][] wind, int sub) {
        int lonLength = lon / sub + 1;
        int latLength = lat / sub + 1;
        int windLength = wind[0].length;

        //NC数据 lat在外，lon在内，不需要调换lat
        double[] arr = new double[lonLength * latLength];
        for (int i = 0, n = 0; i < windLength; i += sub, n++) {
            //第i个经度数组
            short[] arr1 = wind[0][i];

            //清洗数据后的数组
            double[] arr2 = new double[lonLength - 1];

            for (int j = 0, m = 0; j < arr1.length; j += sub, m++) {

                short sh = (arr1[j] == -32767 ? 0 : arr1[j]);

                double val = sh * 0.0010621605509104192 + 1.1575595057707537;

                arr2[m] = (val == -32767 ? 0 : val);
            }

            System.arraycopy(arr2, 0, arr, n * arr2.length, arr2.length);

//            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }
        return arr;
    }


}
