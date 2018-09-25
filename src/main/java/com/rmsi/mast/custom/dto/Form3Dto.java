package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.domain.LandUseType;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class Form3Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region;

    private String province;

    private String commune;

    private String village;

    private long parcel_no;

    private String lot_no;

    private int section_no;

    private Double area;

    private String firstname;

    private String lastname;

    private String address;

    private Date dob;

    private String birthplace;

    private String share;

    private List<LandUseType> existing_use;

    private String neighbor_north;

    private String neighbor_east;

    private String neighbor_west;

    private String neighbor_south;

    private Date public_notice_startdate;

    private Date public_notice_enddate;

    private String cfv_president;

    private String familyname;

    private Date contradictory_date;

    private Date contradictory_time;

    private String other_use;

    private boolean flag;

    private int tennancytypeID;

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

    public String getLot_no() {
        return lot_no;
    }

    public void setLot_no(String lot_no) {
        this.lot_no = lot_no;
    }

    public int getSection_no() {
        return section_no;
    }

    public void setSection_no(int section_no) {
        this.section_no = section_no;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public List<LandUseType> getExisting_use() {
        return existing_use;
    }

    public void setExisting_use(List<LandUseType> existingList) {
        this.existing_use = existingList;
    }

    public String getNeighbor_north() {
        return neighbor_north;
    }

    public void setNeighbor_north(String neighbor_north) {
        this.neighbor_north = neighbor_north;
    }

    public String getNeighbor_east() {
        return neighbor_east;
    }

    public void setNeighbor_east(String neighbor_east) {
        this.neighbor_east = neighbor_east;
    }

    public String getNeighbor_west() {
        return neighbor_west;
    }

    public void setNeighbor_west(String neighbor_west) {
        this.neighbor_west = neighbor_west;
    }

    public String getNeighbor_south() {
        return neighbor_south;
    }

    public void setNeighbor_south(String neighbor_south) {
        this.neighbor_south = neighbor_south;
    }

    public String getCfv_president() {
        return cfv_president;
    }

    public void setCfv_president(String cfv_president) {
        this.cfv_president = cfv_president;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getPublic_notice_startdate() {
        return public_notice_startdate;
    }

    public void setPublic_notice_startdate(Date public_notice_startdate) {
        this.public_notice_startdate = public_notice_startdate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getPublic_notice_enddate() {
        return public_notice_enddate;
    }

    public void setPublic_notice_enddate(Date public_notice_enddate) {
        this.public_notice_enddate = public_notice_enddate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getContradictory_date() {
        return contradictory_date;
    }

    public void setContradictory_date(Date contradictory_date) {
        this.contradictory_date = contradictory_date;
    }

    public Date getContradictory_time() {
        return contradictory_time;
    }

    public void setContradictory_time(Date date) {
        this.contradictory_time = date;
    }

    public String getOther_use() {
        return other_use;
    }

    public void setOther_use(String other_use) {
        this.other_use = other_use;
    }

    public long getParcel_no() {
        return parcel_no;
    }

    public void setParcel_no(long parcel_no) {
        this.parcel_no = parcel_no;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getTennancytypeID() {
        return tennancytypeID;
    }

    public void setTennancytypeID(int tennancytypeID) {
        this.tennancytypeID = tennancytypeID;
    }

}
