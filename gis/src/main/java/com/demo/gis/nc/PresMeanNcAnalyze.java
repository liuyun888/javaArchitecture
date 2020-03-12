package com.demo.gis.nc;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;

/**
 * @program: javaArchitecture
 * @description: 气压
 * @author: LiuYunKai
 * @create: 2020-03-12 11:56
 **/
public class PresMeanNcAnalyze {
    public static void main(String[] args) {
        try {
            NetcdfFile ncfile = null;
            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\气压-NC\\PresMeanSeaLevel201803\\PresMeanSeaLevel_EC20180301.nc");
            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
            String var1 = "longitude";
            String var2 = "latitude";
            String var4 = "msl";


            Variable v1 = ncfile.findVariable(var1);
            Variable v2 = ncfile.findVariable(var2);
            Variable v4 = ncfile.findVariable(var4);

            float[] lon = (float[]) v1.read().copyToNDJavaArray();
            float[] lat = (float[]) v2.read().copyToNDJavaArray();

            float[][] swh = (float[][]) v4.read().copyToNDJavaArray();

        } catch (IOException e1) {// TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
