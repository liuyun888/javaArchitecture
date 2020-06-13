package com.java.spc.Indexes;

public class ProcCapIndexes {
    double SL;
    double USL;
    double LSL;
    double mean;
    double stdDev;
    double Ca;
    double Cp;
    double Cpu;
    double Cpl;
    double Cpk;
    double Cc;
    double CR;
    double Cpm;
    /**
     * 初始化过程能力指数
     * @param SL 规格标准
     * @param USL 规格上限
     * @param LSL 规格下限
     * @param mean 组内平均值
     * @param stdDev 组估计标准偏差
     */
    public ProcCapIndexes(double SL, double USL, double LSL, double mean, double stdDev) {
        this.SL = SL;
        this.USL = USL;
        this.LSL = LSL;
        this.mean = mean;
        this.stdDev = stdDev;
        this.Ca = (mean - SL) / Math.max(USL - SL, SL - LSL);
        this.Cp = (USL - LSL) / (6 * stdDev);
        this.Cpu = (USL - mean) / (3 * stdDev);
        this.Cpl = (mean - LSL) / (3 * stdDev);
        this.Cpk = Math.min(Cpu, Cpl);
        this.Cc = this.mean/USL;
        this.CR = 1/Cp;
        this.Cpm = (USL-LSL)/(6*Math.sqrt(Math.pow(stdDev,2)+Math.pow((mean-SL),2)));
    }

    public double getSL() {
        return SL;
    }

    public void setSL(double SL) {
        this.SL = SL;
    }

    public double getUSL() {
        return USL;
    }

    public void setUSL(double USL) {
        this.USL = USL;
    }

    public double getLSL() {
        return LSL;
    }

    public void setLSL(double LSL) {
        this.LSL = LSL;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getCa() {
        return Ca;
    }

    public void setCa(double ca) {
        Ca = ca;
    }

    public double getCp() {
        return Cp;
    }

    public void setCp(double cp) {
        Cp = cp;
    }

    public double getCpu() {
        return Cpu;
    }

    public void setCpu(double cpu) {
        Cpu = cpu;
    }

    public double getCpl() {
        return Cpl;
    }

    public void setCpl(double cpl) {
        Cpl = cpl;
    }

    public double getCpk() {
        return Cpk;
    }

    public void setCpk(double cpk) {
        Cpk = cpk;
    }

    public double getCc() {
        return Cc;
    }

    public void setCc(double cc) {
        Cc = cc;
    }

    public double getCR() {
        return CR;
    }

    public void setCR(double CR) {
        this.CR = CR;
    }

    public double getCpm() {
        return Cpm;
    }

    public void setCpm(double cpm) {
        Cpm = cpm;
    }
}
