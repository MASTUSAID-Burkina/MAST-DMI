package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.domain.fetch.SpatialunitPersonwithinterest;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class Form7Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region;

    private String province;

    private String commune;

    private String village;

    private String village_no;

    private Date application_date;

    private int application_year;

    private String application_month;

    private int application_dd;

    private String application_no;

    private String name;

    private String address;

    private String profession;

    private String cfv_president;

    private String location;

    private String area;

    private String neighbour_north;
    private String neighbour_east;
    private String neighbour_west;
    private String neighbour_south;

    private String family_name;

    private Date date_recognition_rights;

    private Date public_issuansedate;

    private double area_ares;

    private double area_centiares;

    private String application_type;

    private List<SpatialunitPersonwithinterest> poiLst;

    private String pv_no;

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

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCfv_president() {
        return cfv_president;
    }

    public void setCfv_president(String cfv_president) {
        this.cfv_president = cfv_president;
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

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDate_recognition_rights() {
        return date_recognition_rights;
    }

    public void setDate_recognition_rights(Date date_recognition_rights) {
        this.date_recognition_rights = date_recognition_rights;
    }

    public List<SpatialunitPersonwithinterest> getPoiLst() {
        return poiLst;
    }

    public void setPoiLst(List<SpatialunitPersonwithinterest> poiLst) {
        this.poiLst = poiLst;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getPublic_issuansedate() {
        return public_issuansedate;
    }

    public void setPublic_issuansedate(Date public_issuansedate) {
        this.public_issuansedate = public_issuansedate;
    }

    public double getArea_ares() {
        return area_ares;
    }

    public void setArea_ares(double area_ares) {
        this.area_ares = area_ares;
    }

    public double getArea_centiares() {
        return area_centiares;
    }

    public void setArea_centiares(double area_centiares) {
        this.area_centiares = area_centiares;
    }

    public int getApplication_year() {
        return application_year;
    }

    public void setApplication_year(int application_year) {
        this.application_year = application_year;
    }

    public String getApplication_month() {
        return application_month;
    }

    public void setApplication_month(String application_month) {
        this.application_month = application_month;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApplication_date() {
        return application_date;
    }

    public void setApplication_date(Date application_date) {
        this.application_date = application_date;
    }

    public int getApplication_dd() {
        return application_dd;
    }

    public void setApplication_dd(int application_dd) {
        this.application_dd = application_dd;
    }

    public String getApplication_type() {
        return application_type;
    }

    public void setApplication_type(String application_type) {
        this.application_type = application_type;
    }

    public String getPv_no() {
        return pv_no;
    }

    public void setPv_no(String pv_no) {
        this.pv_no = pv_no;
    }

}
