package com.java.spc.Charts;


import com.java.spc.CalUtils.ConstantValues;
import com.java.spc.CalUtils.VariablesCalculatorUtils;

/**
 * 均值-极差控制图（X-R）
 */
public class XRChart implements IVarChart{
    double groupMean = 0.0;//组平均值
    double distMean = 0.0;//极差
    double[][] arrays;//输入数组
    int size = 0;
    double UCL1;//X控制上限
    double CL1;//X中心线
    double LCL1;//X控制下线
    double UCL2;//R控制上限
    double CL2;//R中心线
    double LCL2;//R控制下线
    VariablesCalculatorUtils var = new VariablesCalculatorUtils();
    public XRChart(double[][] arrays) {
        this.arrays = arrays;
        this.size = arrays[0].length;
        //计算平均值
        this.groupMean = var.getMean(arrays);
        //计算估计标准偏差
        this.distMean = var.getDistMean(arrays);
        //计算UCL
        this.UCL1 = this.groupMean + ConstantValues.A2[size] * this.distMean;
        this.UCL2 = ConstantValues.D4[size] * this.distMean;
        //计算CL
        this.CL1 = this.groupMean;
        this.CL2 = this.distMean;
        //计算LCL
        this.LCL1 = this.groupMean - ConstantValues.A2[size] * this.distMean;
        this.LCL2 = ConstantValues.D3[size] * this.distMean;
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
