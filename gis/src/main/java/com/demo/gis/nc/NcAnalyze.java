package com.demo.gis.nc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @program: javaArchitecture
 * @description: NC文件分析
 * @author: LiuYunKai
 * @create: 2020-02-26 14:53
 **/
public class NcAnalyze {

//    public static void main(String[] args) {
//        // read完copyToNDJavaArray，这个读出来是几维转的就是几维，直接转相应几维java数组就好。
//
//        try {
//            NetcdfFile ncfile = null;
//            ncfile = NetcdfDataset.open("E:\\workspace\\GIS\\数据\\NC文件解析\\Current202002241624.nc");
//            // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
//            String var1 = "lon";
//            String var2 = "lat";
//            Variable v1 = ncfile.findVariable(var1);
//            Variable v2 = ncfile.findVariable(var2);
//
//            // 因为经纬度是一维的，直接 copyToNDJavaArray结果就是一维的，强转一下就好，然后java的一维数组大家该咋用咋用就行了
//            // 二维、三维一样原理
//            float[] lon = (float[]) v1.read().copyToNDJavaArray();
//            float[] lat = (float[]) v2.read().copyToNDJavaArray();
//
//        } catch (IOException e1) {// TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//    }

//    public static void main(String[] args) {
//        long begin = 1519833600000L;
//        long end = 1522508400000L;
//        int size = Math.round((end - begin) / 3600000);
//        //经过处理之后数据是一一对应的了
//        for (int i = 0; i <= size; i++) {
//            long time = 3600000L * i;
//
//            System.out.println(begin + time + "第" + i + "次循环");
//        }
//    }

    public static void main(String[] args) {

        String str = "";

        System.out.println(JSON.isValid(str));
        System.out.println(isJson(str));
    }

    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
