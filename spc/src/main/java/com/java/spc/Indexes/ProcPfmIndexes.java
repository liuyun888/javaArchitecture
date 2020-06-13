package com.java.spc.Indexes;

public class ProcPfmIndexes {
    double SL;
    double USL;
    double LSL;
    double mean;
    double stdDev;
    double Pp;
    double Ppu;
    double Ppl;
    double Ppk;
    double PR;
    double Ppm;

    /**
     * 初始化过程表现指数
     * @param SL 规格标准
     * @param USL 规格上限
     * @param LSL 规格下限
     * @param mean 组内平均值
     * @param stdDev 组估计标准偏差
     */
    public ProcPfmIndexes(double SL, double USL, double LSL, double mean, double stdDev) {
        this.SL = SL;
        this.USL = USL;
        this.LSL = LSL;
        this.mean = mean;
        this.stdDev = stdDev;
        this.Pp = (USL - LSL) / (6 * stdDev);
        this.Ppu = (USL - mean) / (3 * stdDev);
        this.Ppl = (mean - LSL) / (3 * stdDev);
        this.Ppk = Math.min(Ppu, Ppl);
        this.PR = 1 / Pp;
        this.Ppm = (USL - LSL) / (6 * Math.sqrt(Math.pow(stdDev, 2) + Math.pow(mean - SL, 2)));
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

    public double getPp() {
        return Pp;
    }

    public void setPp(double pp) {
        Pp = pp;
    }

    public double getPpu() {
        return Ppu;
    }

    public void setPpu(double ppu) {
        Ppu = ppu;
    }

    public double getPpl() {
        return Ppl;
    }

    public void setPpl(double ppl) {
        Ppl = ppl;
    }

    public double getPpk() {
        return Ppk;
    }

    public void setPpk(double ppk) {
        Ppk = ppk;
    }

    public double getPR() {
        return PR;
    }

    public void setPR(double PR) {
        this.PR = PR;
    }

    public double getPpm() {
        return Ppm;
    }

    public void setPpm(double ppm) {
        Ppm = ppm;
    }
}
