package com.demo.gis.entity;

import java.lang.Double;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 群任务--任务信息
 *
 * @author Administrator
 */

@Data
@Document(collection = "typhoon_water_depth")
public class TyphoonWaterDepth {
    @Id
    private String id;
    private Double longitude;//经度
    private Double latitude;//纬度
    private Double object_id_1;
    private Double join_count;
    private Double target_fid;
    private Double object_id;
    private Double rc_id;
    private Integer prim;
    private Integer crup;
    private Integer objl;
    private Integer rver;
    private Integer agen;
    private Double fidn;
    private Integer fids;
    private String lnam;
    private Double expsou;
    private String nobjnm;
    private String objnam;
    private String quasou;
    private Double souacc;
    private String tecsou;
    private Double verdat;
    private String status;
    private String inform;
    private String ntxtds;
    private Double scamax;
    private Double scamin;
    private String txtdsc;
    private String recdat;
    private String sordat;
    private String sorind;
    private Double depth;//水深

}
