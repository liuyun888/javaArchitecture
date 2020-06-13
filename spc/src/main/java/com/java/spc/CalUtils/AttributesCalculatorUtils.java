package com.java.spc.CalUtils;

public class AttributesCalculatorUtils {

    /**
     *  一维数组求和
     * @param arrays 一维数组
     * @return 和
     */
    public double getSum(double[] arrays) {
        double sum = 0.0;
        for (double array : arrays) {
            sum += array;
        }
        return sum;
    }

    /**
     * 获取一维数组平均值
     * @param arrays  一维数组
     * @return 平均值
     */
    public double getMean(double[] arrays) {
        double sum = 0.0;
        for (double array : arrays) {
            sum += array;
        }
        return sum / arrays.length;
    }

    /**
     * (不良率管制图） P图
     * 计算不合格数均值，适配范围：每组样本容量相同
     *
     * @param fails 每组缺陷数
     * @return
     */
    public double getPAvg(double[] fails) {
        return this.getMean(fails);
    }
    /**
     * (不良率管制图） P图
     * 计算不合格数均值，适配范围：主要用于每组样本数不同情况，每组样本数相同与样本数不同均可适用
     *
     * @param samples 每组样本容量
     * @param fails   每组缺陷数
     * @return 不合格数均值
     */
    public double getPAvg(double[] samples, double[] fails) {
        double sum = 0.0;
        double count = 0.0;
        for (int i = 0; i < samples.length; i++) {
            sum += samples[i] * fails[i];
            count += samples[i];
        }
        return sum / count;
    }

    /**
     * (不良数管制图）Pn图
     * 计算每组平均不良数
     * @param arrays 一维数组，每组中部和个配件的数量
     * @return 平均不良数
     */
    public double getPnAvg(double[] arrays) {
        return this.getSum(arrays) / arrays.length;
    }

    /**
     *（缺点数管制图）C图
     * 计算每组平均不合格点数
     * @param arrays 一维数组，每组中不合格配件的数量
     * @return 平均缺点数
     */
    public double getCAvg(double[] arrays) {
        return this.getSum(arrays) / arrays.length;
    }

    /**
     * (单位缺点数管制图）U图
     * 计算每组平均缺点数
     *
     * @param fails 每组样本中的缺陷数
     * @param samples 每组样本的容量
     * @return 各单位平均缺点数
     */
    public double getUAvg(double[] samples, double[] fails) {
        double sum = this.getSum(fails);
        double count = this.getSum(samples);
        return sum / count;
    }

}
