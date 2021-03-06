package com.java.spc.CalUtils;

import org.junit.Test;

public class ConstantValues {
    public static double[] A2 = new double[]{0, 0, 1.880, 1.023, 0.729, 0.577, 0.483, 0.419, 0.373, 0.337, 0.308, 0.285, 0.266, 0.249, 0.235, 0.223, 0.212, 0.203, 0.194, 0.187, 0.180, 0.173, 0.167, 0.162, 0.157, 0.153};

    public static double[] D2 = {1.0, 1.0, 1.128, 1.693, 2.059, 2.326, 2.543, 2.704, 2.847, 2.970, 3.078, 3.173, 3.258, 3.336, 3.407, 3.472, 3.532, 3.588, 3.640, 3.689, 3.735, 3.778, 3.819, 3.858, 3.895, 3.931};

    public static double[] D3 = new double[]{0, 0, 0, 0, 0, 0, 0, 0.076, 0.136, 0.184, 0.223, 0.256, 0.283, 0.307, 0.328, 0.347, 0.363, 0.378, 0.391, 0.403, 0.415, 0.425, 0.434, 0.443, 0.451, 0.459};

    public static double[] D4 = new double[]{0, 0, 3.267, 2.574, 2.282, 2.114, 2.004, 1.924, 1.864, 1.816, 1.777, 1.717, 1.693, 1.672, 1.653, 1.637, 1.622, 1.608, 1.597, 1.585, 1.575, 1.566, 1.557, 1.548, 1.541};

    public static double[] A3 = new double[]{0, 0, 2.659, 1.954, 1.628, 1.427, 1.287, 1.182, 1.099, 1.032, 0.975, 0.927, 0.886, 0.850, 0.817, 0.789, 0.763, 0.739, 0.718, 0.698, 0.680, 0.663, 0.647, 0.633, 0.619, 0.606};
    //                                   n   0 1      2      3      4      5      6      7      8      9     10     11     12     13     14     15       20     25
    public static double[] c4 = new double[]{0, 0, 0.7979, 0.8862, 0.9213, 0.9400, 0.9515, 0.9594, 0.9650, 0.9693, 0.9727, 0.9754, 0.9776, 0.9794, 0.9810, 0.9823, 0.9835, 0.9845, 0.9854, 0.9862, 0.9869, 0.9876, 0.9882, 0.9887, 0.9892, 0.9896}; //,0.9869,0.9896};

    public static double[] B3 = new double[]{0, 0, 0, 0, 0, 0, 0.030, 0.118, 0.185, 0.239, 0.284, 0.321, 0.354, 0.382, 0.406, 0.428, 0.448, 0.446, 0.482, 0.497, 0.510, 0.523, 0.534, 0.545, 0.555, 0.565}; //, 0.510, 0.565};

    public static double[] B4 = new double[]{0, 0, 3.276, 2.568, 2.266, 2.089, 1.970, 1.882, 1.815, 1.761, 1.716, 1.679, 1.646, 1.618, 1.594, 1.572, 1.552, 1.534, 1.518, 1.503, 1.490, 1.477, 1.466, 1.455, 1.445, 1.435}; //, 1.490, 1.435};

    //    public static double[] B5 = new double[]{0,0,     0,     0,     0,     0, 0.029, 0.113, 0.179, 0.232, 0.276, 0.313, 0.346, 0.374, 0.399, 0.421}; //, 0.504, 0.559};
//    public static double[] B6 = new double[]{0,0, 2.606, 2.276, 2.088, 1.964, 1.874, 1.806, 1.751, 1.707, 1.669, 1.637, 1.610, 1.585, 1.563, 1.544}; //, 1.470, 1.420};

    @Test
    public void test() {
        System.out.println(A2.length);
        System.out.println(D2.length);
        System.out.println(D3.length);
        System.out.println(D4.length);
        System.out.println(A3.length);
        System.out.println(c4.length);
        System.out.println(B3.length);
        System.out.println(B4.length);
    }
}
