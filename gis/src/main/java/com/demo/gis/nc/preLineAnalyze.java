package com.demo.gis.nc;

import net.minidev.json.JSONObject;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wcontour.Contour;
import wcontour.global.Border;
import wcontour.global.PointD;
import wcontour.global.PolyLine;
import wcontour.global.Polygon;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: javaArchitecture
 * @description: 气压等值线
 * @author: LiuYunKai
 * @create: 2020-03-16 10:24
 **/
public class preLineAnalyze {

    public static void main(String[] args) throws IOException {
        isolineProcess();
    }


    //生成等值线主方法
    public static Map<String, Object> isolineProcess() throws IOException {
        Map<String, Object> resMap = new HashMap<String, Object>();
        //文件路径
        String filePath = "D:\\GIS\\数据\\气压-NC\\PresMeanSeaLevel201803\\PresMeanSeaLevel_EC20180325.nc";
        //nc变量名
        String element = "msl";
        //nc文件深度序列
        int depthIndex = 1;
        //nc文件时间序列
        int timeIndex = 1;
        //获取NC的数据
        Map map = getNcData(filePath, element, depthIndex, timeIndex);
        if (map == null || map.size() == 0) {
            return resMap;
        }
        //等值线等值面间隔,间隔越小:过渡越均匀,性能消耗越大,响应越慢
        double[] dataInterval = new double[]{995,1000,1005,1010,1015,1020,1025,1030};
        String strGeojson = nc2EquiSurface(map, dataInterval);
        resMap.put("geojson", strGeojson);
        resMap.put("dataArr", map.get("eleData"));
        return resMap;
    }

    // 获取nc数据
    public static <T> Map getNcData(String ncpath, String element, int depthIndex, int timeIndex) throws IOException {

        //加载nc文件
        NetcdfFile ncfile = NetcdfDataset.open(ncpath);
        //读取经纬度数据
        String var1 = "longitude";
        String var2 = "latitude";
        String var4 = "msl";

        Variable v1 = ncfile.findVariable(var1);
        Variable v2 = ncfile.findVariable(var2);
        Variable v4 = ncfile.findVariable(var4);

        float[] lon = (float[]) v1.read().copyToNDJavaArray();
        float[] lat = (float[]) v2.read().copyToNDJavaArray();

        float[][] msl = (float[][]) v4.read().copyToNDJavaArray();

        return parseFloatToDouble(lon, lat, msl);
    }

    //处理nc经纬度
    private static Map parseFloatToDouble(float[] lonArr, float[] latArr, float[][] msl) {
        Map map = new HashMap();

        //创建double数组存放经纬度数据，方便后续计算
        double[] dLon = new double[lonArr.length], dLat = new double[latArr.length];
        double[][] ele = new double[latArr.length][lonArr.length];
        for (int i = 0, len = lonArr.length; i < len; i++) {
            dLon[i] = Double.parseDouble(String.valueOf(lonArr[i]));
        }
        for (int i = 0, len = latArr.length; i < len; i++) {
            dLat[i] = Double.parseDouble(String.valueOf(latArr[i]));
        }

        for (int i = 0; i < latArr.length; i++) {
            for (int j = 0; j < lonArr.length; j++) {

                ele[i][j] = Double.parseDouble(String.valueOf(msl[i][j]/100));

            }
        }

        map.put("lon", dLon);
        map.put("lat", dLat);
        map.put("eleData", ele);
        return map;
    }


    //nc数据生成等值线数据
    public static String nc2EquiSurface(Map ncData, double[] dataInterval) {
        String geojsonpogylon = "";

        List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
        List<Polygon> cPolygonList = new ArrayList<Polygon>();

        double[][] _gridData = (double[][]) ncData.get("eleData");
        int[][] S1 = new int[_gridData.length][_gridData[0].length];
        double[] _X = (double[]) ncData.get("lon"), _Y = (double[]) ncData.get("lat");
        double _undefData = -9999.0;
        List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y, S1, _undefData);
        int nc = dataInterval.length;
        // 生成等值线
        cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc, dataInterval, _undefData, _borders, S1);
        // 平滑
        cPolylineList = Contour.smoothLines(cPolylineList);
        cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList, _borders, dataInterval);

        geojsonpogylon = getPolygonGeoJson(cPolygonList);

        return geojsonpogylon;
    }

    //拼装geogson
    public static String getPolygonGeoJson(List<Polygon> cPolygonList) {
        String geo = null;
        String geometry = " { \"type\":\"Feature\",\"geometry\":";
        String properties = ",\"properties\":{ \"value\":";

        String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
        String end = "  ] }";
        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        try {
            for (Polygon pPolygon : cPolygonList) {
                List<Object> ptsTotal = new ArrayList<Object>();
                for (PointD ptd : pPolygon.OutLine.PointList) {
                    List<Double> pt = new ArrayList<Double>();
                    pt.add(doubleFormat(ptd.X));
                    pt.add(doubleFormat(ptd.Y));
                    ptsTotal.add(pt);
                }
                List<Object> list3D = new ArrayList<Object>();
                list3D.add(ptsTotal);
                JSONObject js = new JSONObject();
                js.put("type", "Polygon");
                js.put("coordinates", list3D);

                geo = geometry + js.toString() + properties + pPolygon.LowValue + "} }" + "," + geo;
            }
            if (geo.contains(",")) {
                geo = geo.substring(0, geo.lastIndexOf(","));
            }

            geo = head + geo + end;
        } catch (Exception e) {
            e.printStackTrace();
            return geo;
        }
        return geo;
    }

    /**
     * double保留两位小数
     */
    public static double doubleFormat(double d) {
        BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    public static float[] JoinArrayForTwo(float[] lon, float[] lat, float[][] current) {
        //NC数据 lat在外，lon在内，需要调换lat
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < current.length; i++) {

            float[] arr1 = current[current[0].length - 1 - i];

            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }
        return arr;
    }
}
