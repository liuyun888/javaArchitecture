package com.demo.gis.service.impl;
import	java.util.ArrayList;

import com.demo.gis.entity.TyphoonWaterDepth;
import com.demo.gis.repository.TyphoonWaterDepthRepository;
import com.demo.gis.service.WaterDepthService;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.demo.gis.shp.WaterDepthShpAnalyze2.readShp;

/**
 * @program: javaArchitecture
 * @description: 水深
 * @author: LiuYunKai
 * @create: 2020-03-04 15:56
 **/
@Service
public class WaterDepthServiceImpl implements WaterDepthService {

    @Autowired
    private TyphoonWaterDepthRepository typhoonWaterDepthRepository;

    @Override
    public List<TyphoonWaterDepth> addWaterDeptInfo(String path) {

        path = "E:\\workspace\\GIS\\数据\\水深 -- sbn\\水深点\\50.shp";

        //读取shp
        SimpleFeatureCollection colls1 = readShp(path);
        //拿到所有features
        SimpleFeatureIterator iters = colls1.features();
        //遍历打印
        System.out.println("==========开始打印===========");

        List<TyphoonWaterDepth> list = new ArrayList<> ();

        while (iters.hasNext()) {
            SimpleFeature sf = iters.next();

            System.out.println(sf.getID() + " , " + sf.getAttributes());

            List<Object> attributes;
            attributes = sf.getAttributes();

            System.out.println("point:" + attributes.get(0));

            System.out.println("depth:" + attributes.get(30));

            Point point = (Point) attributes.get(0);

            TyphoonWaterDepth waterDepth = new TyphoonWaterDepth();

            waterDepth.setId( String.valueOf( point.getX()) +  String.valueOf( point.getY()));
            waterDepth.setLongitude(point.getX());
            waterDepth.setLatitude(point.getY());
//            waterDepth.setObject_id_1(Double.valueOf(attributes.get(1).toString()) );


            waterDepth.setDepth(Double.parseDouble(attributes.get(30).toString()));
            list.add(waterDepth);

            break;
        }

        System.out.println("==========结束打印===========");


//        return "";
        return typhoonWaterDepthRepository.saveAll(list);
    }
}
