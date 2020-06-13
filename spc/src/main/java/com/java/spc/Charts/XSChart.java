package com.java.spc.Charts;


import com.java.spc.CalUtils.ConstantValues;
import com.java.spc.CalUtils.VariablesCalculatorUtils;

/**
 * 均值-标准差控制图（X-S）
 */
public class XSChart implements IVarChart{
    double groupMean = 0.0;//组平均值
    double stdDev = 0.0;//标准偏差
    double[][] arrays;//输入数组
    int size = 0;
    double UCL1;//X控制上限
    double CL1;//X中心线
    double LCL1;//X控制下线
    double UCL2;//S控制上限
    double CL2;//S中心线
    double LCL2;//S控制下线
    VariablesCalculatorUtils var = new VariablesCalculatorUtils();
    public XSChart(double[][] arrays) {
        this.arrays = arrays;
        this.size = arrays[0].length;
        //计算总平均值
        this.groupMean = var.getMean(arrays);
        //计算总标准偏差
        this.stdDev = var.getGroupStdDev(arrays);
        //计算UCL
        this.UCL1 = this.groupMean + ConstantValues.A3[size] * this.stdDev;
        this.UCL2 = ConstantValues.B4[size] * this.stdDev;
        //计算CL
        this.CL1 = this.groupMean;
        this.CL2 = this.stdDev;
        //计算LCL
        this.LCL1 = this.groupMean - ConstantValues.A3[size] * this.stdDev;
        this.LCL2 = ConstantValues.B3[size] * this.stdDev;
    }


    public double getGroupMean() {
        return groupMean;
    }

    @Override
    public double getUCL1() {
        return this.UCL1;
    }

    @Override
    public double getCL1() {
        return this.CL1;
    }

    @Override
    public double getLCL1() {
        return this.LCL1;
    }

    @Override
    public double getUCL2() {
        return this.UCL2;
    }

    @Override
    public double getCL2() {
        return this.CL2;
    }

    @Override
    public double getLCL2() {
        return this.LCL2;
    }

}
