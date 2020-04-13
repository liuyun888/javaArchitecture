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
            double[] arr2 = new double[lonLength - 1];

            for (int j = 0, index = 0; j < arr1.length; j += sub, index++) {
                if (WIND.equals(name)) {
                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 0.0010621605509104192 + 1.1575595057707537;
                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;

                    arr2[index] = (val == -32767 ? 0 : val);
                } else if (AIR_TEM.equals(name)) {
                    arr2[index] = (double) ((arr1[j] == -32767.0 ? 0 : arr1[j]) + 273.15);
                } else if (PRES.equals(name)) {
                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 0.1983218378526849 + 99766.52583908108;
                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;
                    arr2[index] = (val == -32767 ? 0 : val);

                } else if (SALT.equals(name)) {
                    arr2[index] = (arr1[j] == 9999.0 ? 0 : arr1[j]);
                } else if (WAVE.equals(name)) {

                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 2.1466761940023993E-4 + 7.064921819790635;

                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;
                    arr2[index] = (val == -32767 ? 0 : val);

                } else if (SEA_TEM.equals(name)) {
                    short sh = (arr1[j] == -32767 ? 0 : arr1[j]);
                    double val = sh * 5.947959007770894E-4 + 289.21857467236214;
                    //保留小数4位
                    val = (double) Math.round(val * 10000) / 10000;

                    //单位为 K
                    arr2[index] = (val == -32767 ? 0 : val);
                } else if (CURRENT.equals(name)) {
                    arr2[index] = (arr1[j] == -999.0 ? 0 : arr1[j]);
                }
            }

            System.arraycopy(arr2, 0, arr, index2 * arr2.length, arr2.length);
        }
        return arr;
    }


    public static double[] JoinArrayForTwoForCurrent(int lon, int lat, float[][] current, int sub) {
        int latLength = lat / sub + 1;
        int lonLength = lon / sub + 1;
        int currentLength = current.length - 1;

        //NC数据 lat在外，lon在内，需要调换lat
        int index2 = 0;
        double[] arr = new double[latLength * lonLength];
        for (int i = 0; i < currentLength; i += sub) {

            //长度为601的数组
            float[] arr1 = current[i];

            //清洗数据后的数组
            double[] arr2 = new double[latLength];
            int index = 0;

            for (int j = 0; j < arr1.length; j++) {
                if (j % sub != 0) {
                    continue;
                }
                //保留小数点后四位
                double val = (double) Math.round(arr1[j] * 10000) / 10000;
                arr2[index] = (arr1[j] == -999.0 ? 0 : val);

//                arr2[index] = arr1[j];
                index++;
            }

            System.arraycopy(arr2, 0, arr, index2 * arr2.length, arr2.length);
            index2++;
        }
        return arr;
    }

    public static double[] JoinArrayForWave(int lon, int lat, double[][][] dataArray, int sub) {
        int latLength = lat / sub + 1;
        int lonLength = lon / sub + 1;

        //NC数据 lat在外，lon在内，不需要调换lat
        double[] arr = new double[latLength * lonLength];
        for (int i = 0, index2 = 0; i < latLength; i++, index2++) {
            //第i个经度数组
            double[] arr1 = dataArray[0][i];

            //清洗数据后的数组
            double[] arr2 = new double[lonLength];

            for (int j = 0; j < lonLength; j++) {
                //保留小数4位
                double val = (double) Math.round(arr1[j] * 1000000) / 1000000;
                arr2[j] = (val == 1.01E-4 ? 0 : val);
            }

            System.arraycopy(arr2, 0, arr, index2 * arr2.length, arr2.length);
        }
        return arr;
    }
}
