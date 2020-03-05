package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 盐度NC文件解析
 * @author: LiuYunKai
 * @create: 2020-02-26 17:06
 **/
public class SaltNcAnalyze {
    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\盐度 -- NC\\Salt.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            String var3 = "month";
            String var4 = "SALT";


            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            //time ---> :timezone
            Variable v3 = ncfile.findVariable(var3);
            //:missing_value  ---> Double
            Variable v4 = ncfile.findVariable(var4);

            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
            // 二维、三维一样原理
            double[] lon = (double[]) v1.read().copyToNDJavaArray();
            double[] lat = (double[]) v2.read().copyToNDJavaArray();

            double[] month = (double[]) v3.read().copyToNDJavaArray();
            float[][][] SALT = (float[][][]) v4.read().copyToNDJavaArray();

            System.out.println("盐度NC文件解析");
            System.out.println("lon :" + Arrays.toString(lon));
            System.out.println("lon.length :" + lon.length);
            System.out.println("=====================================");
            System.out.println("lat :" + Arrays.toString(lat));
            System.out.println("lat.length :" + lat.length);
            System.out.println("=====================================");
            System.out.println("month :" + Arrays.toString(month));
            System.out.println("month.length :" + month.length);
            System.out.println("=====================================");
            System.out.println("SALT :" + Arrays.toString(SALT));
            System.out.println("SALT.length :" + SALT.length);
            System.out.println("SALT[0].length :" +SALT[0].length);
            System.out.println("SALT[0][0]:" + SALT[0][0].length);
//            for(int i=0;i<SALT[0].length;i++){
                System.out.println("SALT[0][0]:" + Arrays.toString(SALT[0][0]));
//            }


        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
