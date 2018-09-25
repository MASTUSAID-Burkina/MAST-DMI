package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.domain.LandUseType;
import com.rmsi.mast.studio.domain.fetch.SpatialunitPersonwithinterest;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class Form8Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region;

    private String province;

    private String commune;

    private String village;

    private String village_no;

    private Date application_date;

    private String application_no;

    private String familyname;

    private String family_address;

    private String first_name;

    private String last_name;

    private String birthplace;

    private Date dob;

    private String sex;

    private String refrence_id_card;

    private String profession;

    private String address;

    private String id_card_date_place;

    private String location;

    private String area;

    private String neighbour_north;
    private String neighbour_east;
    private String neighbour_west;
    private String neighbour_south;

    private List<LandUseType> existing_use;

    private String mayor_name;

    private String pv_no;

    private Date date_recognition_right;

    private Date apfr_date;

    private String refrenceof_mandate;

    private Double area_ares;
    private Double area_centiares;

    private List<SpatialunitPersonwithinterest> poiLst;

    private String other_use;

    private String apfrno;

    private Date mandateDate;
    private Date idcardEstbDate;

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

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillage_no() {
        return village_no;
    }

    public void setVillage_no(String village_no) {
        this.village_no = village_no;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApplication_date() {
        return application_date;
    }

    public void setApplication_date(Date application_date) {
        this.application_date = application_date;
    }

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRefrence_id_card() {
        return refrence_id_card;
    }

    public void setRefrence_id_card(String refrence_id_card) {
        this.refrence_id_card = refrence_id_card;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNeighbour_north() {
        return neighbour_north;
    }

    public void setNeighbour_north(String neighbour_north) {
        this.neighbour_north = neighbour_north;
    }

    public String getNeighbour_east() {
        return neighbour_east;
    }

    public void setNeighbour_east(String neighbour_east) {
        this.neighbour_east = neighbour_east;
    }

    public String getNeighbour_west() {
        return neighbour_west;
    }

    public void setNeighbour_west(String neighbour_west) {
        this.neighbour_west = neighbour_west;
    }

    public String getNeighbour_south() {
        return neighbour_south;
    }

    public void setNeighbour_south(String neighbour_south) {
        this.neighbour_south = neighbour_south;
    }

    public List<LandUseType> getExisting_use() {
        return existing_use;
    }

    public void setExisting_use(List<LandUseType> existingList) {
        this.existing_use = existingList;
    }

    public String getMayor_name() {
        return mayor_name;
    }

    public void setMayor_name(String mayor_name) {
        this.mayor_name = mayor_name;
    }

    public String getPv_no() {
        return pv_no;
    }

    public void setPv_no(String pv_no) {
        this.pv_no = pv_no;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDate_recognition_right() {
        return date_recognition_right;
    }

    public void setDate_recognition_right(Date date_recognition_right) {
        this.date_recognition_right = date_recognition_right;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApfr_date() {
        return apfr_date;
    }

    public void setApfr_date(Date apfr_date) {
        this.apfr_date = apfr_date;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getFamily_address() {
        return family_address;
    }

    public void setFamily_address(String family_address) {
        this.family_address = family_address;
    }

    public String getId_card_date_place() {
        return id_card_date_place;
    }

    public void setId_card_date_place(String id_card_date_place) {
        this.id_card_date_place = id_card_date_place;
    }

    public String getRefrenceof_mandate() {
        return refrenceof_mandate;
    }

    public void setRefrenceof_mandate(String refrenceof_mandate) {
        this.refrenceof_mandate = refrenceof_mandate;
    }

    public List<SpatialunitPersonwithinterest> getPoiLst() {
        return poiLst;
    }

    public void setPoiLst(List<SpatialunitPersonwithinterest> poiLst) {
        this.poiLst = poiLst;
    }

    public Double getArea_ares() {
        return area_ares;
    }

    public void setArea_ares(Double area_ares) {
        this.area_ares = area_ares;
    }

    public Double getArea_centiares() {
        return area_centiares;
    }

    public void setArea_centiares(Double area_centiares) {
        this.area_centiares = area_centiares;
    }

    public String getOther_use() {
        return other_use;
    }

    public void setOther_use(String other_use) {
        this.other_use = other_use;
    }

    public String getApfrno() {
        return apfrno;
    }

    public void setApfrno(String apfrno) {
        this.apfrno = apfrno;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getMandateDate() {
        return mandateDate;
    }

    public void setMandateDate(Date mandateDate) {
        this.mandateDate = mandateDate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getIdcardEstbDate() {
        return idcardEstbDate;
    }

    public void setIdcardEstbDate(Date idcardEstbDate) {
        this.idcardEstbDate = idcardEstbDate;
    }

}
