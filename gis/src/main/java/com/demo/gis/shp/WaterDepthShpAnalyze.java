package com.demo.gis.shp;

import org.geotools.data.shapefile.ShapefileDataStore;

import org.geotools.data.shapefile.ShapefileDataStoreFactory;

import org.geotools.data.simple.SimpleFeatureIterator;

import org.geotools.data.simple.SimpleFeatureSource;

import org.opengis.feature.Property;

import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @program: javaArchitecture
 * @description: 水深SHP文件解析
 * @author: LiuYunKai
 * @create: 2020-03-04 10:56
 **/
public class WaterDepthShpAnalyze {

    public static void main(String[] args) {

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        try {

            ShapefileDataStore sds = (ShapefileDataStore)dataStoreFactory.createDataStore(new File("E:\\workspace" +
                    "\\GIS\\数据\\水深 -- sbn\\水深点\\50.shp").toURI().toURL());

            sds.setCharset(Charset.forName("GBK"));

            SimpleFeatureSource featureSource = sds.getFeatureSource();

            SimpleFeatureIterator itertor = featureSource.getFeatures().features();



            while(itertor.hasNext()) {

                SimpleFeature feature = itertor.next();

                Iterator<Property> it = feature.getProperties().iterator();



                while(it.hasNext()) {

                    Property pro = it.next();

                    System.out.println(pro);

                }

            }

            itertor.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }





}
