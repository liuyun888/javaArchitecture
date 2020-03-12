package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 海浪
 * @author: LiuYunKai
 * @create: 2020-03-12 11:33
 **/
public class WaveNcAnalyze {

    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\海浪-NC\\Wave201803.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "longitude";
            String var2 = "latitude";
            String var3 = "time";
            String var4 = "swh";
            String var5 = "mwd";
            String var6 = "mwp";


            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            Variable v3 = ncfile.findVariable(var3);
            Variable v4 = ncfile.findVariable(var4);
            Variable v5 = ncfile.findVariable(var5);
            Variable v6 = ncfile.findVariable(var6);

            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

            int[] time = (int[]) v3.read().copyToNDJavaArray();

            short[][][] swh = (short[][][]) v4.read().copyToNDJavaArray();
            short[][][] mwd = (short[][][]) v5.read().copyToNDJavaArray();
            short[][][] mwp = (short[][][]) v6.read().copyToNDJavaArray();

        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
