package com.demo.gis.nc;

import com.demo.gis.common.constant.Constant;
import com.demo.gis.common.toolUtil.CalendarUtils;
import com.demo.gis.common.toolUtil.FiledNcAnalyze;
import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.entity.HotFieldHeader;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @program: javaArchitecture
 * @description: 全球海浪
 * @author: LiuYunKai
 * @create: 2020-04-10 18:02
 **/
public class EarthWaveNcAnalyze implements Constant {

    public static void main(String[] args) throws IOException, ParseException {
        NetcdfFile ncFile = NetcdfDataset.open("D:\\GIS\\数据\\海浪-NC\\全球\\ecwmf_era5_wave_grid0.5_areafull_date2018_03_1to2018_03_31.nc");
        // 存经纬度 // 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面 ncfile 那行看
        String var1 = "longitude";
        String var2 = "latitude";
        String var3 = "time";
        String var4 = "mwd";
        String var5 = "swh";

        Variable v1 = ncFile.findVariable(var1);
        Variable v2 = ncFile.findVariable(var2);
        //time ---> :timezone
        Variable v3 = ncFile.findVariable(var3);
        //:missing_value  ---> Double
        Variable v4 = ncFile.findVariable(var4);
        Variable v5 = ncFile.findVariable(var5);

        float[] lon = (float[]) v1.read().copyToNDJavaArray();
        float[] lat = (float[]) v2.read().copyToNDJavaArray();

        int[] time = (int[]) v3.read().copyToNDJavaArray();

        //取得1970/1/1距离1900/1/1的小时数
        int hours = CalendarUtils.getHourNum();

        for (int i = 0; i < time.length; i++) {

            long ts = (time[i] - hours - 8) * 60 * 60;

            Date date = CalendarUtils.getDateByTimeStamp(ts);

            int sub = 40;

            int[] origin = new int[]{i, 0, 0};
            int[] size = new int[]{1, lat.length, lon.length};

            try {
                short[][][] mwd = (short[][][]) v4.read(origin, size).copyToNDJavaArray();

                short[][][] swh = (short[][][]) v5.read(origin, size).copyToNDJavaArray();

                HotFieldHeader UType = HotFieldHeader.InitTyphoonInfoByLatForEarth(lon, lat, ts, sub, 2, U_WAVE);
                HotFieldHeader VType = HotFieldHeader.InitTyphoonInfoByLatForEarth(lon, lat, ts, sub, 3, V_WAVE);


                //根据波高、波向计算UV
                Map<String, double[]> map = getUV(lon, lat, swh, mwd, sub);
                //开辟新数组长度为两数组之和
                double[] UArr = map.get("UArr");
                double[] VArr = map.get("VArr");

                String json = InitJson(UType, VType, UArr, VArr, date);

                System.out.println(json);
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, double[]> getUV(float[] lon, float[] lat, short[][][] swh, short[][][] mwd, int sub) {

        int latLength = lat.length;
        int lonLength = lon.length;
        Map map = new HashMap<>(latLength / sub * lonLength / sub);
        double[][][] uf = new double[1][latLength / sub + 1][lonLength / sub + 1];
        double[][][] vf = new double[1][latLength / sub + 1][lonLength / sub + 1];
        //NC数据 lat在外，lon在内，不需要调换lat
        for (int i = 0, x = 0; i < latLength; i += sub, x++) {
            for (int j = 0, y = 0; j < lonLength; j += sub, y++) {

                short sh = (mwd[0][i][j] == -32767 ? 0 : mwd[0][i][j]);
                double dir = sh * 0.005493474122534881 + 179.99918229043232;

                short sh2 = (swh[0][i][j] == -32767 ? 0 : swh[0][i][j]);
                double speed = sh2 * 2.1466761940023993E-4 + 7.064921819790635;

                //获得弧度
                double radian = 2 * Math.PI / 360 * dir;
                double ud = speed * Math.sin(radian);
                double vd = speed * Math.cos(radian);

                uf[0][x][y] = (ud == -32522.76 ? 0 : ud);
                vf[0][x][y] = (vd == -32522.76 ? 0 : vd);
            }
        }
        //开辟新数组长度为两数组之和
        double[] UArr = FiledNcAnalyze.JoinArrayForWave(lonLength, latLength, uf, sub);
        double[] VArr = FiledNcAnalyze.JoinArrayForWave(lonLength, latLength, vf, sub);

        map.put("UArr", UArr);
        map.put("VArr", VArr);

        return map;
    }

    public static String InitJson(HotFieldHeader UType, HotFieldHeader VType, double[] UArr, double[] VArr, Date date) {
        List infoList = new ArrayList<>();
        Map<String, Object> UInfoMap = new HashMap<>(4);
        UInfoMap.put("header", UType);
        UInfoMap.put("data", UArr);
        UInfoMap.put("meta", date);
        infoList.add(UInfoMap);

        Map<String, Object> VInfoMap = new HashMap<>(4);
        VInfoMap.put("header", VType);
        VInfoMap.put("data", VArr);
        VInfoMap.put("meta", date);
        infoList.add(VInfoMap);

        Map<String, Object> data = new HashMap<>(1);
        data.put("dataJson", infoList);
        String jsonStr = JacksonUtils.toJSONString(data);

        return jsonStr;
    }
}
