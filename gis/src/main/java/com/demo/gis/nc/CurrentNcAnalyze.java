package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 流场NC文件分析
 * @author: LiuYunKai
 * @create: 2020-02-26 16:51
 **/
public class CurrentNcAnalyze {
    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("D:\\GIS\\数据\\流场 -- NC\\03\\01\\Current201803010000.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            String var3 = "U";
            String var4 = "V";


            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            //time ---> :timezone
            Variable v3 = ncfile.findVariable(var3);
            //:missing_value  ---> Double
            Variable v4 = ncfile.findVariable(var4);

            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
            // 二维、三维一样原理
            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

            float[][] U = (float[][]) v3.read().copyToNDJavaArray();
            float[][] V = (float[][]) v4.read().copyToNDJavaArray();


            System.out.println("流场NC文件分析");
            System.out.println("lon :" + Arrays.toString(lon));
            System.out.println("lon.length :" + lon.length);
            System.out.println("=====================================");
            System.out.println("lat :" + Arrays.toString(lat));
            System.out.println("lat.length :" + lat.length);
            System.out.println("=====================================");
            System.out.println("U :" + Arrays.toString(U));
            System.out.println("U.length :" + U.length);
            System.out.println("U[0] :" + U[0].length);
            System.out.println("U[0] :" + Arrays.toString(U[0]));
            System.out.println("=====================================");
            System.out.println("V :" + Arrays.toString(V));
            System.out.println("V.length :" + V.length);
            System.out.println("V[0] :" + V[0].length);
            System.out.println("V[0] :" + Arrays.toString(V[0]));
            System.out.println("=====================================");



        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
