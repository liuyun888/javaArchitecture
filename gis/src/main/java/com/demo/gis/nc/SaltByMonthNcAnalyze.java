package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 月均盐度
 * @author: LiuYunKai
 * @create: 2020-03-17 10:25
 **/
public class SaltByMonthNcAnalyze {
    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\盐度 -- NC\\月均盐度\\cora1.0_regional_201701_s.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            String var3 = "depth";//35
            //:missing_value：9999.0， [[[f depth,lat,lon
            String var4 = "salt";
            String var5 = "year";
            String var6 = "month";

            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            Variable v3 = ncfile.findVariable(var3);
            Variable v4 = ncfile.findVariable(var4);
            Variable v5 = ncfile.findVariable(var5);
            Variable v6 = ncfile.findVariable(var6);

            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

            float[] depth = (float[]) v3.read().copyToNDJavaArray();
            float[][][] SALT = (float[][][]) v4.read().copyToNDJavaArray();

            int[] year = (int[]) v5.read().copyToNDJavaArray();
            int[] month = (int[]) v6.read().copyToNDJavaArray();
            System.out.println(year[0]);
            System.out.println(month[0]);



        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
