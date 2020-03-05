package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;

/**
 * @program: javaArchitecture
 * @description: NC文件分析
 * @author: LiuYunKai
 * @create: 2020-02-26 14:53
 **/
public class NcAnalyze {

    public static void main(String[] args) {
        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。

        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\NC文件解析\\Current202002241624.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "lon";
            String var2 = "lat";
            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);

            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
            // 二维、三维一样原理
            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
