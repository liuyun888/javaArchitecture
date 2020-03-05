package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.util.Arrays;

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
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\风场  -- NC\\RA.gfs.wind.2019080102.nc");
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
            float[][][] windu = (float[][][])  v4.read().copyToNDJavaArray();
            float[][][] windv = (float[][][])  v5.read().copyToNDJavaArray();

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
//            for(int i=0;i<windu[0].length;i++){
//                System.out.println("windu[0][" + i + "]:" + Arrays.toString(windu[0][i]));
//            }
            System.out.println("windu[0][0].length :" + windu[0][0].length);
            System.out.println("windu[0][0]:" + Arrays.toString(windu[0][0]));

        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
