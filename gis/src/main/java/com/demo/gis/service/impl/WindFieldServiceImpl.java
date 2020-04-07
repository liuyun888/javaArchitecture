package com.demo.gis.service.impl;

import com.demo.gis.common.constant.Constant;
import com.demo.gis.common.toolUtil.FileUtils;
import com.demo.gis.common.toolUtil.JacksonUtils;
import com.demo.gis.repository.TyphoonWindFieldRepository;
import com.demo.gis.service.WindFieldService;
import com.demo.gis.vo.TyphoonWindFieldVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ucar.ma2.DataType;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @program: ops-china
 * @description: 风场
 * @author: LiuYunKai
 * @create: 2020-03-09 09:23
 **/
@Service
public class WindFieldServiceImpl implements WindFieldService, Constant {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");


    @Autowired
    private TyphoonWindFieldRepository typhoonWindFieldRepository;

    @Override
    public boolean analyzeNCFile(MultipartFile file) {

        String filePath = "";
        String timeStr = file.getOriginalFilename().split("\\.")[3];

        filePath = saveFileToLocal(file);

        return analyzeNc(filePath, timeStr);
    }

    private boolean analyzeNc(String filePath, String timeStr) {
        NetcdfFile ncFile = null;
        try {

            ncFile = NetcdfDataset.open(filePath);


            Variable v1 = ncFile.findVariable(LON);
            Variable v2 = ncFile.findVariable(LAT);
            //time ---> :timezone
            Variable v3 = ncFile.findVariable(TIME);
            //:missing_value  ---> Double
            Variable v4 = ncFile.findVariable(WIND_U);
            Variable v5 = ncFile.findVariable(WIND_V);

            DataType type = v1.getDataType();

            String jsonStr = "";
            if (type.name().equals(FLOAT)) {
                float[] lon = (float[]) v1.read().copyToNDJavaArray();
                float[] lat = (float[]) v2.read().copyToNDJavaArray();
                int[] time = (int[]) v3.read().copyToNDJavaArray();
                float[][][] windu = (float[][][]) v4.read().copyToNDJavaArray();
                float[][][] windv = (float[][][]) v5.read().copyToNDJavaArray();

                TyphoonWindFieldVo UType = initUTyphoonInfo(lon, lat, time);
                TyphoonWindFieldVo VType = initVTyphoonInfo(lon, lat, time);

                //开辟新数组长度为两数组之和
                float[] UArr = joinArray(lon, lat, windu);
                float[] VArr = joinArray(lon, lat, windv);

                //组装JSON
                jsonStr = initJson(UType, VType, UArr, VArr);


            } else if (type.name().equals(DOUBLE)) {
                double[] lon = (double[]) v1.read().copyToNDJavaArray();
                double[] lat = (double[]) v2.read().copyToNDJavaArray();
                double[] time = (double[]) v3.read().copyToNDJavaArray();
                float[][][] windu = (float[][][]) v4.read().copyToNDJavaArray();
                float[][][] windv = (float[][][]) v5.read().copyToNDJavaArray();

                TyphoonWindFieldVo UType = initUTyphoonInfoForDouble(lon, lat, time);
                TyphoonWindFieldVo VType = initVTyphoonInfoForDouble(lon, lat, time);

                //开辟新数组长度为两数组之和
                float[] UArr = joinArrayForDouble(lon, lat, windu);
                float[] VArr = joinArrayForDouble(lon, lat, windv);

                //组装JSON
                jsonStr = initJson(UType, VType, UArr, VArr);
            }

            //保存到mongo
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
            Date date = sdf.parse(timeStr);
            long ts = date.getTime();

//            TyphoonWindField typhoonWindField = new TyphoonWindField();
//            typhoonWindField.setId(timeStr);
//            typhoonWindField.setTime(ts);
//            typhoonWindField.setJson(jsonStr);
//
//
//            typhoonWindFieldRepository.save(typhoonWindField);

            return true;
        } catch (Exception e) {
            logger.error("解析失败:" + e.getMessage());
        } finally {
            try {
                if (ncFile != null) {
                    ncFile.close();
                }

            } catch (IOException e) {
                logger.error("NC文件关闭异常:" + e.getMessage());
            }

            //将之前上传的apk删除
            String localPath = filePath;
            System.out.println(localPath);
            File file2 = new File(localPath);
            if (file2.exists()) {
                Boolean result = file2.delete();
                System.out.println(result);
            }

        }
        return false;
    }

    @Override
    public boolean analyzeNCZIPFile(MultipartFile originalFile) {
//
        try {
            File file = multipartFileToFile(originalFile);

            List<String> resultFilePathList = handleZipFile(file);
            for (String path : resultFilePathList) {
                String timeStr = path.split("\\.")[3];
                if(!analyzeNc(path, timeStr)){
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<String> handleZipFile(File originFile) {
        // 读取 PDF 文件
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(originFile);
        } catch (IOException e) {
            logger.error("打开 zip 文件失败", e);
            return null;
        }

        // 目标文件的 List
        List<File> resultFileList = new ArrayList<>(32);
        List<String> resultFilePathList = new ArrayList<>(32);
        // 重新定义临时目录
        final String tmpdir = TMP_DIR + "current" + File.separator;

        // ZipEntry 迭代
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
        try {
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                // 源文件名
                String fileName = zipEntry.getName();
                // 文件 URL
                final String destPath = tmpdir + fileName;
                File originalFile = new File(destPath);


                // 直接将此文件下载到临时目录并加入 resultFileList
                FileUtils.downloadFileLocal(zipFile.getInputStream(zipEntry), destPath);
                // 加入进去
                resultFileList.add(originalFile);
                resultFilePathList.add(destPath);
                continue;


            }
        } catch (IOException e) {
            logger.error("从 ZipFile 中 根据 ZipEntry 获取 InputStream 流异常", e);
//            Assert.fail(String.format("从 ZipFile 中 根据 ZipEntry 获取 InputStream 流异常:[%s]", e.getMessage()));
            FileUtils.deleteFiles(resultFileList);
            return null;
        } finally {
            try {
                zipFile.close();
            } catch (IOException e) {
                logger.error("关闭流时异常", e);
            }
        }

        return resultFilePathList;
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }


    private String saveFileToLocal(MultipartFile file) {

        String[] names = file.getOriginalFilename().split("\\.");
        String name = names[0] + names[1] + names[2];
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + name;

        String filePath = basePath + File.separator + file.getOriginalFilename();
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.mkdirs();
        }
        try {
            file.transferTo(desFile);
        } catch (IllegalStateException | IOException e) {
            logger.error("NC文件保存异常:" + e.getMessage());
        }

        return filePath;
    }

    private String initJson(TyphoonWindFieldVo UType, TyphoonWindFieldVo VType, float[] UArr, float[] VArr) {
        List infoList = new ArrayList<>();
        Map<String, Object> UInfoMap = new HashMap<>(4);
        UInfoMap.put("header", UType);
        UInfoMap.put("data", UArr);
        infoList.add(UInfoMap);

        Map<String, Object> VInfoMap = new HashMap<>(4);
        VInfoMap.put("header", VType);
        VInfoMap.put("data", VArr);
        infoList.add(VInfoMap);

        Map<String, Object> data = new HashMap<>(1);
        data.put("dataJson", infoList);
        String jsonStr = JacksonUtils.toJSONString(data);

        return jsonStr;
    }


    private TyphoonWindFieldVo initVTyphoonInfo(float[] lon, float[] lat, int[] time) {
        TyphoonWindFieldVo VType = new TyphoonWindFieldVo();
        VType.setLo1(lon[0]);
        VType.setLa2(lat[0]);
        VType.setLo2(lon[lon.length - 1]);
        VType.setLa1(lat[lat.length - 1]);
        VType.setDx(lon[1] - lon[0]);
        VType.setDy(lat[1] - lat[0]);
        VType.setNx(lon.length);
        VType.setNy(lat.length);
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time[0]);
        VType.setRefTime(timeStr);
        VType.setParameterUnit("m/s");
        VType.setParameterNumber(3);
        VType.setParameterNumberName("eastward_wind");
        VType.setParameterCategory(2);

        return VType;
    }

    private TyphoonWindFieldVo initUTyphoonInfo(float[] lon, float[] lat, int[] time) {
        TyphoonWindFieldVo UType = new TyphoonWindFieldVo();
        UType.setLo1(lon[0]);
        UType.setLa2(lat[0]);
        UType.setLo2(lon[lon.length - 1]);
        UType.setLa1(lat[lat.length - 1]);
        UType.setDx(lon[1] - lon[0]);
        UType.setDy(lat[1] - lat[0]);
        UType.setNx(lon.length);
        UType.setNy(lat.length);
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time[0]);
        UType.setRefTime(timeStr);
        UType.setParameterUnit("m/s");
        UType.setParameterNumber(2);
        UType.setParameterNumberName("eastward_wind");
        UType.setParameterCategory(2);
        return UType;
    }

    private TyphoonWindFieldVo initVTyphoonInfoForDouble(double[] lon, double[] lat, double[] time) {
        TyphoonWindFieldVo VType = new TyphoonWindFieldVo();
        VType.setLo1(lon[0]);
        VType.setLa2(lat[0]);
        VType.setLo2(lon[lon.length - 1]);
        VType.setLa1(lat[lat.length - 1]);
        VType.setDx(lon[1] - lon[0]);
        VType.setDy(lat[1] - lat[0]);
        VType.setNx(lon.length);
        VType.setNy(lat.length);
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time[0]);
        VType.setRefTime(timeStr);
        VType.setParameterUnit("m/s");
        VType.setParameterNumber(3);
        VType.setParameterNumberName("eastward_wind");
        VType.setParameterCategory(2);

        return VType;
    }

    private TyphoonWindFieldVo initUTyphoonInfoForDouble(double[] lon, double[] lat, double[] time) {
        TyphoonWindFieldVo UType = new TyphoonWindFieldVo();
        UType.setLo1(lon[0]);
        UType.setLa2(lat[0]);
        UType.setLo2(lon[lon.length - 1]);
        UType.setLa1(lat[lat.length - 1]);
        UType.setDx(lon[1] - lon[0]);
        UType.setDy(lat[1] - lat[0]);
        UType.setNx(lon.length);
        UType.setNy(lat.length);
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time[0]);
        UType.setRefTime(timeStr);
        UType.setParameterUnit("m/s");
        UType.setParameterNumber(2);
        UType.setParameterNumberName("eastward_wind");
        UType.setParameterCategory(2);
        return UType;
    }

    private static float[] joinArray(float[] lon, float[] lat, float[][][] wind) {

        //NC数据 lat在外，lon在内，需要调换lat
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < wind[0].length; i++) {

            float[] arr1 = wind[0][wind[0].length - 1 - i];

//            float[] arr2 = reverseArray(arr1);

            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }

        return arr;
    }

    private static float[] joinArrayForDouble(double[] lon, double[] lat, float[][][] wind) {

        //NC数据 lat在外，lon在内，需要调换lat
        float[] arr = new float[lon.length * lat.length];
        for (int i = 0; i < wind[0].length; i++) {

            float[] arr1 = wind[0][wind[0].length - 1 - i];

            System.arraycopy(arr1, 0, arr, i * arr1.length, arr1.length);
        }

        return arr;
    }
}
