package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.domain.fetch.SpatialunitPersonwithinterest;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class Form2Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String family_name = "";
    private Date dateofMandate;
    private String location = "";
    private String address = "";
    private Date idEstablishDate;
    private String village = "";
    private String commune = "";
    private String region = "";
    private String province = "";
    private String firstname = "";
    private String lastname = "";
    private Date birthdate;
    private String birthplace = "";
    private String refrenceID = "";
    private String mandate_origin = "";
    private List<SpatialunitPersonwithinterest> poiLst;

    private String cfvname;

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDateofMandate() {
        return dateofMandate;
    }

    public void setDateofMandate(Date dateofMandate) {
        this.dateofMandate = dateofMandate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getIdEstablishDate() {
        return idEstablishDate;
    }

    public void setIdEstablishDate(Date idEstablishDate) {
        this.idEstablishDate = idEstablishDate;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public List<SpatialunitPersonwithinterest> getPoiLst() {
        return poiLst;
    }

    public void setPoiLst(List<SpatialunitPersonwithinterest> poiLst) {
        this.poiLst = poiLst;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getRefrenceID() {
        return refrenceID;
    }

    public void setRefrenceID(String refrenceID) {
        this.refrenceID = refrenceID;
    }

    public String getMandate_origin() {
        return mandate_origin;
    }

    public void setMandate_origin(String mandate_origin) {
        this.mandate_origin = mandate_origin;
    }

    public String getCfvname() {
        return cfvname;
    }

    public void setCfvname(String cfvname) {
        this.cfvname = cfvname;
    }

}
