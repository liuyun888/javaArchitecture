package com.java.spc.Charts;


import com.java.spc.CalUtils.VariablesCalculatorUtils;

import java.util.Random;

public class Test {

    @org.junit.Test
    public void xrTest() {

        VariablesCalculatorUtils var = new VariablesCalculatorUtils();
//        System.out.println(var.getMean(new double[]{1.1, 2.3, 4.5}));
//        //二维数组均值
//        System.out.println(var.getMean(doubles));

        //1.取检测数据，以测定顺序排列，通常数据分为20-25组，每组4-5个数据
        Random r = new Random(1);

        double[][] doubles = new double[10][5];
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[0].length; j++) {
                doubles[i][j] =  r.nextInt(100);
            }
        }
        //2.计算各组样本统计量，如样本平均值、极差及总平均值
        //2.1各子组平均值
        double[] means = new double[doubles.length];
        for (int i = 0; i < means.length; i++) {
            means[i] = var.getMean(doubles[i]);
        }

        //2.2各子组极差
        double[] ranges = new double[doubles.length];
        for (int i = 0; i < ranges.length; i++) {
            ranges[i] = var.getRange(doubles[i]);
        }

        //6.1.3 计算控制界限,  均值-极差控制图（X-R）
        XRChart xrChart = new XRChart(doubles);
        System.out.println( "X中心线:"+ xrChart.CL1);
        System.out.println( "X控制上限:"+ xrChart.UCL1);
        System.out.println( "X控制下限:"+ xrChart.LCL1);



    }


    @org.junit.Test
    public void xsTest() {

        VariablesCalculatorUtils var = new VariablesCalculatorUtils();

        //1.取检测数据，以测定顺序排列，通常数据分为20-25组，每组4-5个数据
        double[][] doubles = new double[10][5];
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[0].length; j++) {
                doubles[i][j] = i + j;
            }
        }
        //2.计算各组样本统计量，如样本平均值、标准差及总平均值
        //2.1各子组平均值
        double[] means = new double[doubles.length];
        for (int i = 0; i < means.length; i++) {
            means[i] = var.getMean(doubles[i]);
        }

        //2.2各子组标准差
        double[] stdDevs = new double[doubles.length];
        for (int i = 0; i < stdDevs.length; i++) {
            stdDevs[i] = var.getStdDev(doubles[i]);
        }

        //6.1.3 计算控制界限,  均值-标准差控制图（X-S）
        XRChart xrChart = new XRChart(doubles);
        System.out.println( "X中心线:"+ xrChart.CL1);
        System.out.println( "X控制上限:"+ xrChart.UCL1);
        System.out.println( "X控制下限:"+ xrChart.LCL1);



    }
}
