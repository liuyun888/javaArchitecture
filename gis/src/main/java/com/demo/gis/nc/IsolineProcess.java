package com.demo.gis.nc;

import net.minidev.json.JSONObject;
import org.opengis.coverage.grid.InvalidRangeException;
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
 * @description: NC转等值线
 * @author: LiuYunKai
 * @create: 2020-03-16 16:47
 **/
public class IsolineProcess {

    public static void main(String[] args) throws IOException {
        isolineProcess();
    }

    //生成等值线主方法
    public static Map<String, Object> isolineProcess() throws IOException {
        Map<String, Object> resMap = new HashMap<String, Object>();
        String filePath = "E:\\workspace\\GIS\\数据\\气压-NC\\PresMeanSeaLevel201803\\PresMeanSeaLevel_EC20180301.nc";//文件路径
        String element = "msl";//nc变量名
        int depthIndex = 1;//nc文件深度序列
        int timeIndex = 1;//nc文件时间序列
        //获取NC的数据
        Map map = getNcData(filePath,element,depthIndex,timeIndex);
        if(map == null || map.size()==0){
            return resMap;
        }
        //等值线等值面间隔,间隔越小:过渡越均匀,性能消耗越大,响应越慢
        double[] dataInterval = new double[]{0,1,2,3,4, 5,6,7,8,9, 10,11,12,13,14,15,
                16,17,18,19, 20,21,22,23,24, 25,26,27,28,29, 30, 35};
        String strGeojson = nc2EquiSurface(map, dataInterval);
        resMap.put("geojson", strGeojson);
        resMap.put("dataArr", map.get("eleData"));
        return resMap;
    }
    // 获取nc数据
    public static <T> Map getNcData(String ncpath,String element,int depthIndex,int timeIndex) throws IOException {
        String lonVarName = "longitude";//经度变量
        String latVarName = "latitude";//纬度变量
        //加载nc文件
        NetcdfFile ncfile =  NetcdfDataset.open(ncpath);
        //读取经纬度数据
        Variable varLon = ncfile.findVariable(lonVarName);
        Variable varLat = ncfile.findVariable(latVarName);
        Object lonObj = null;
        Object latObj = null;
        lonObj = (Object) varLon.read().copyToNDJavaArray();
        latObj = (Object) varLat.read().copyToNDJavaArray();
        Map map = new HashMap();
        map = readNCLonLat(map,lonObj,latObj);
        int latLength = (int) map.get("latLength");
        int lonLength = (int) map.get("lonLength");

        //读取变量数据
        Variable varPre = ncfile.findVariable(element);
        int[] org = { 0, 0 };// 每一维的起点,起点从0开始
        int[] sha = { latLength, lonLength };// 相应维读取的条数
        /*
         * nc数据类型可能是short、folat、double，所以用Object接收，通过反射判断数据类型
         */
        Object pre = null;
        try {
            pre = (Object) varPre.read(org,sha).copyToNDJavaArray();
        } catch (InvalidRangeException | ucar.ma2.InvalidRangeException e) {
            e.printStackTrace();
        }
        String className = pre.getClass().getComponentType().getName();

        Double invalidValueDou = -9999.0;//无效值
        Double scaleFactorDou = 1.0;//比例因子数值
        /*
         * 这里只判断了float数组，其他数据类型的判断类似.
         * 我的数据是二维的：经纬度。
         */
        if("[F".equalsIgnoreCase(className)){
            float[][] dataArr = (float[][])pre;
            //创建新的double数组保存数据
            double[][] dPre = new double[dataArr.length][dataArr[0].length];
            //循环将float数组转为double数组
            for (int i = 0, len = dataArr.length; i < len; i++) {
                float[] _pre = dataArr[i];
                for (int j = 0, jlen = _pre.length; j < jlen; j++) {
                    Double value = Double.parseDouble(String.valueOf(_pre[j]));
                    if(invalidValueDou != null && Double.compare(value,invalidValueDou)==0){
                        dPre[i][j] = invalidValueDou;
                        continue;//无效值跳过
                    }
                    if(scaleFactorDou == null){//比例因子处理
                        dPre[i][j] = value;
                    }else{
                        dPre[i][j] = value * scaleFactorDou;
                    }

                }
            }
            map.put("eleData", dPre);
        }
        map.put("invalid",invalidValueDou);
        return map;
    }

    //处理nc经纬度
    private static Map readNCLonLat(Map map, Object lonObj, Object latObj) {
        /**
         * nc数据类型可能是short、folat、double，所以用Object接收，通过反射判断数据类型,
         * 这里只判断了float数组，其他数据类型的判断类似
         */
        String lonclassName = lonObj.getClass().getComponentType().getName();
        int lonLength = 1;
        int latLength = 1;
        if ("float".equalsIgnoreCase(lonclassName)) {
            float[] lonArr = (float[]) lonObj;
            float[] latArr = (float[]) latObj;
            lonLength = lonArr.length;
            latLength = latArr.length;
            //创建double数组存放经纬度数据，方便后续计算
            double[] dLon = new double[lonArr.length], dLat = new double[latArr.length];
            for (int i = 0, len = lonArr.length; i < len; i++) {
                dLon[i] = Double.parseDouble(String.valueOf(lonArr[i]));
            }
            for (int i = 0, len = latArr.length; i < len; i++) {
                dLat[i] = Double.parseDouble(String.valueOf(latArr[i]));
            }
            map.put("lon", dLon);
            map.put("lat", dLat);
        }
        map.put("lonLength",lonLength);
        map.put("latLength",latLength);
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
        double _undefData = (double) ncData.get("invalid");
        List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
                S1, _undefData);
        int nc = dataInterval.length;
        cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
                dataInterval, _undefData, _borders, S1);// 生成等值线

        cPolylineList = Contour.smoothLines(cPolylineList);// 平滑
        cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
                _borders, dataInterval);

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

                geo = geometry + js.toString() + properties  +pPolygon.LowValue + "} }" + "," + geo;
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
}
