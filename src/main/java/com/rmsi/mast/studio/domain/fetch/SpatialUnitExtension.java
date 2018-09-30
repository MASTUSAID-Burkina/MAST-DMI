package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.util.JsonDateSerializer;

@Entity
@Table(name = "spatialunit_extension")
public class SpatialUnitExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SPATIAL_EXTENSION_ID_GENERATOR", sequenceName = "spatial_extension_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPATIAL_EXTENSION_ID_GENERATOR")
    private long id;
    private long usin;
    private String parcelno;
    private String section_no;
    private String lotno;
    private Date parcel_generation_date;
    private Date contradictory_date;
    private Date recognition_rights_date;
    private Date mayor_sign_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsin() {
        return usin;
    }

    public void setUsin(long usin) {
        this.usin = usin;
    }

    public String getParcelno() {
        return parcelno;
    }

    public void setParcelno(String parcelno) {
        this.parcelno = parcelno;
    }

    public String getSection_no() {
        return section_no;
    }

    public void setSection_no(String section_no) {
        this.section_no = section_no;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getParcel_generation_date() {
        return parcel_generation_date;
    }

    public void setParcel_generation_date(Date parcel_generation_date) {
        this.parcel_generation_date = parcel_generation_date;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getContradictory_date() {
        return contradictory_date;
    }

    public void setContradictory_date(Date contradictory_date) {
        this.contradictory_date = contradictory_date;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getRecognition_rights_date() {
        return recognition_rights_date;
    }

    public void setRecognition_rights_date(Date recognition_rights_date) {
        this.recognition_rights_date = recognition_rights_date;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getMayor_sign_date() {
        return mayor_sign_date;
    }

    public void setMayor_sign_date(Date mayor_sign_date) {
        this.mayor_sign_date = mayor_sign_date;
    }
}
