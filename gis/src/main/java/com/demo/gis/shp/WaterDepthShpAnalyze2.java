package com.demo.gis.shp;

import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Filter;

/**
 * @program: javaArchitecture
 * @description: 水深SHP文件解析
 * @author: LiuYunKai
 * @create: 2020-03-04 10:56
 **/
public class WaterDepthShpAnalyze2 {

    public static SimpleFeatureCollection readShp(String path) {
        return readShp(path, null);

    }

    public static SimpleFeatureCollection readShp(String path, Filter filter) {

        SimpleFeatureSource featureSource = readStoreByShp(path);

        if (featureSource == null) {
            return null;
        }

        try {
            return filter != null ? featureSource.getFeatures((Query) filter) : featureSource.getFeatures();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static SimpleFeatureSource readStoreByShp(String path) {

        File file = new File(path);

        FileDataStore store;
        SimpleFeatureSource featureSource = null;
        try {
            store = FileDataStoreFinder.getDataStore(file);
            ((ShapefileDataStore) store).setCharset(Charset.forName("UTF-8"));
            featureSource = store.getFeatureSource();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return featureSource;
    }

    public static void main(String[] args) {
        String path1 = "E:\\workspace\\GIS\\数据\\水深 -- sbn\\水深点\\50.shp";

        //读取shp
        SimpleFeatureCollection colls1 = readShp(path1);
        //拿到所有features
        SimpleFeatureIterator iters = colls1.features();
        //遍历打印
        System.out.println("==========开始打印===========");

        while (iters.hasNext()) {
            SimpleFeature sf = iters.next();

            System.out.println(sf.getID() + " , " + sf.getAttributes());

            List<Object> attributes = sf.getAttributes();

            System.out.println("point:" + attributes.get(0));

            System.out.println("depth:" + attributes.get(30));

            Point point = (Point) attributes.get(0);

            Double longitude = point.getX();
            Double latitude = point.getY();

            //todo 只打印一次
            break;

        }

        System.out.println("==========结束打印===========");
    }

}
