package com.java.spc.CalUtils;

import java.util.Arrays;

public class VariablesCalculatorUtils {

    /**
     * 获取一维数组最大值
     * @param arrays 一维数组
     * @return 最大值
     */
    public double getMax(double[] arrays) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < arrays.length; i++) {
            max = Math.max(max, arrays[i]);
        }
        return max;
    }

    /**
     * 获取一维数组最小值
     * @param arrays  一维数组
     * @return 最小值
     */
    public double getMin(double[] arrays) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < arrays.length; i++) {
            min = Math.min(min, arrays[i]);
        }
        return min;
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
     * 获取平均值
     * @param arrays  二维数组
     * @return 获取二维数组平均值
     */
    public double getMean(double[][] arrays) {
        double sum = 0.0;
        for (double[] array : arrays) {
            sum += this.getMean(array);
        }
        return sum / arrays.length;
    }

    /**
     * 计算极差
     * @param max 最大值
     * @param min 最小值
     * @return 极差
     */
    public double getRange(double max, double min) {
        return max - min;
    }

    /**
     * 计算一维数组极差
     * @param arrays 一维数组
     * @return 极差
     */
    public double getRange(double[] arrays) {
        return this.getMax(arrays) - this.getMin(arrays);
    }

    /**
     * 计算二维数组平均极差（全距平均值）
     * @param dists 二维数组
     * @return 平均极差
     */
    public double getDistMean(double[][] dists) {
        double sum = 0.0;
        for (double[] dist : dists) {
            sum += getRange(dist);
        }
        return sum / dists.length;
    }

    /**
     * 计算一维数组标准偏差
     * @param arrays 一维数组
     * @return 标准偏差
     */
    public double getStdDev(double[] arrays) {
        double mean = this.getMean(arrays);
        double sum = 0.0;
        for (double array : arrays) {
            sum += Math.pow(array - mean, 2);
        }
        return Math.sqrt(sum / arrays.length - 1);
    }

    /**
     * 计算二维数组标准偏差
     * @param groups 二维数组
     * @return 标准偏差
     */
    public double getGroupStdDev(double[][] groups) {
        double sum = 0.0;
        for (double[] group : groups) {
            sum += this.getStdDev(group);
        }
        return sum / groups.length;
    }

    /**
     * 计算移动全距
     * @param x1 参数x1
     * @param x2 参数x2
     * @return 移动全距
     */
    public double getMovingRange(double x1, double x2) {
        return Math.abs(x1 - x2);
    }

    /**
     * 计算平均移动全距
     * @param arrays 一维移动全距数组
     * @return 平均移动全距
     */
    public double getMovingRangeAvg(double[] arrays) {
        double sum = 0.0;
        for (int i = 0; i < arrays.length - 1; i++) {
            sum += getMovingRange(arrays[i], arrays[i + 1]);
        }
        return sum / arrays.length;
    }

    /**
     * 组内标准差，应用情况：子组数量>1
     * 组内标准差计算方式：sigmaWithin = Sp/C4(d+1), d为Sp的自由度
     * @param arrays 二维数组， 存储多组数据
     * @return 组内标准差
     */
    public double getWithinGroupsStdDev(double[][] arrays) {
        double[] mean = new double[arrays.length];
        for (int i = 0; i < mean.length; i++) {
            mean[i] = getMean(arrays[i]);
        }
        double sum = 0;
        int divSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            int tempSum = 0;
            for (int j = 0; j < arrays[i].length; j++) {
                tempSum += Math.pow(arrays[i][j] - mean[i], 2);
            }
            sum += tempSum;
            divSum += arrays[i].length - 1;
        }
        double Sp = sum / (double) divSum;
        //此处可能存在缺陷，自由度可能超过系数表的规定容量25个,如果超出容量则需要更多维的表或使用gamma函数直接计算。
        double sigmaWithin = Sp / ConstantValues.c4[divSum + 1];
        return sigmaWithin;
    }

    /**
     *  子组间标准差，应用情况：子组数量=1
     *  子组间标准差计算方式：sigmaBetween = Max(0,Math.sqrt((sigma(XBAR))^2-(sigma(within)^2/batch size)))
     *  步长w默认为2，即每次只在相邻的节点间计算
     *  其中：sigma(XBAR)计算方式有如下三种：
     *  0. 移动极差平均值；常用于子组数为1的情况
     *  1. 移动极差中位数；常用于子组数为1的情况
     *  2. 递差军方和平方根（MSSD）
     * @param array 一维数组，采集样本数组
     * @return 子组间标准差
     */
    public double getBetweenGroupsStdDev(double[] array) {
        double sigmaBetween = 0;//子组间标准差
        int w = 2;//步长
        int type = 0;//sigma(XBAR)的计算方式，默认使用1：移动极差平均值,使用RBar计算
        if (type == 0) {
            //计算组内极差均值
            double rangeSum = 0;//极差和
            for (int i = 1; i < array.length; i++) {
                rangeSum += getRangeByPos(array, i);
            }
            sigmaBetween = rangeSum / (array.length + w - 1);
        } else if (type == 1) {
            //计算极差中位数
            double[] tempRanges = new double[array.length - 1];//极差数组,因为是相邻差所以数量比array少1
            for (int i = 1; i < array.length; i++) {
                tempRanges[i - 1] = getRangeByPos(array, i);
            }
            Arrays.sort(tempRanges);//从小到大排序获取中位数
            //根据数组个数的奇偶性计算中位数
            double medianRange = tempRanges.length % 2 == 0 ? (tempRanges[tempRanges.length / 2] + tempRanges[(tempRanges.length / 2) - 1]) : tempRanges[tempRanges.length / 2];
            sigmaBetween = medianRange / ConstantValues.D4[w];
        } else if (type == 2) {
            //计算递差均方和平方根 MSSD
            double rangeSum = 0;
            for (int i = 1; i < array.length; i++) {
                rangeSum += Math.pow(getRangeByPos(array, i), 2);
                sigmaBetween = Math.sqrt(0.5 * rangeSum / (array.length - 1)) / ConstantValues.c4[array.length];
            }
        }
        return sigmaBetween;
    }

    /**
     * 根据位置计算步长内的极差
     * 步长默认为2，即每次只在相邻的节点间计算
     * @param array 一维数组，样本观测值
     * @param pos 起始计算位置
     * @return 数组在步长内的极差
     */
    public double getRangeByPos(double[] array, int pos) {
        return Math.abs(Math.max(array[pos], array[pos - 1]) - Math.min(array[pos], array[pos - 1]));
    }

    /**
     * 组间/组内标准差
     * @param arrays 二维样本数组，一维为组数，二维为组内样本数
     * @param pos 所使用的用来计算组间标准差的一维数组在二维数组arrays中的位置
     * @return 组间/组内标准差
     */
    public double getBetweenAndWithinStdDev(double[][] arrays,int pos) {
        return Math.sqrt(Math.pow(getBetweenGroupsStdDev(arrays[pos]), 2) + Math.pow(getWithinGroupsStdDev(arrays), 2));
    }

    /**
     * 整体标准差
     * @param arrays 二维样本数组，一维为组数，二维为组内样本数
     * @return 整体标准差
     */
    public double getOverallStdDev(double[][] arrays) {
        double[] mean = new double[arrays.length];
        for (int i = 0; i < mean.length; i++) {
            mean[i] = getMean(arrays[i]);
        }
        double sum = 0;
        int divSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            int tempSum = 0;
            for (int j = 0; j < arrays[i].length; j++) {
                tempSum += Math.pow(arrays[i][j] - mean[i], 2);
            }
            sum += tempSum;
            divSum += arrays[i].length;
        }
        return sum / ((double) divSum-1);
    }





/*     public double getEstStdDev(double distMean, int size, String chart, String type) {
        if (chart.equals("XR")) {
            if (type.equals("X")) {
                return distMean / Constants.D2[size];
            } else if (type.equals("R")) {
                return distMean * Constants.D3[size] / Constants.D2[size];
            } else {
                System.err.println("Unknown Type");
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

    public double getCL(double[] groups) {
        return this.getMean(groups);
    }

   public double getUCL(double groupAvg, int size, double rangeAvg, String chart, String type) {
        if (chart.equals("XR")) {
            if (type.equals("X")) {
                return groupAvg + Constants.A2[size] * rangeAvg;
            } else if (type.equals("R")) {
                return Constants.D4[size] * rangeAvg;
            } else {
                System.err.println("Unknown Type");
            }
        } else {
            return 0.0;
        }
    }

    public double getUCL(double groupAvg, int size, double rangeAvg, String chart, String type) {
        if (chart.equals("XR")) {
            if (type.equals("X")) {
                return groupAvg - Constants.A2[size] * rangeAvg;
            } else if (type.equals("R")) {
                return Constants.D3[size] * rangeAvg;
            } else {
                System.err.println("Unknown Type");
            }
        } else {
            return 0.0;
        }
    }*/
}