package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class Form1Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region="";
    private String province="";
    private String commune="";
    private Date dateofapplication;
    private String village="";
    private String villageno="";
    private String applicationno="";
    private String typeoftenancy="";
    private String natureofapplication="";
    private String firstname="";
    private String lastname="";
    private Date birthdate;
    private String birthplace="";
    private String profession="";
    private String address="";
    private String referenceofId="";
    private String fathername="";
    private String mothername="";
    private String maritialStatus="";
    private String natureofpower="";
    private Date issuancedate;
    private String location="";
    private String area;
    private String neighbour_north="";
    private String neighbour_south="";
    private String neighbour_east="";
    private String neighbour_west="";
    private Date idcardestablishment_date;
    private String idcard_origin="";
    private Date mandate_establishmentDate;
    private String mandate_location="";
    private String cfvname="";
    private int tenancyId;

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

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getDateofapplication() {
        return dateofapplication;
    }

    public void setDateofapplication(Date dateofapplication) {
        this.dateofapplication = dateofapplication;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillageno() {
        return villageno;
    }

    public void setVillageno(String villageno) {
        this.villageno = villageno;
    }

    public String getApplicationno() {
        return applicationno;
    }

    public void setApplicationno(String applicationno) {
        this.applicationno = applicationno;
    }

    public String getTypeoftenancy() {
        return typeoftenancy;
    }

    public void setTypeoftenancy(String typeoftenancy) {
        this.typeoftenancy = typeoftenancy;
    }

    public String getNatureofapplication() {
        return natureofapplication;
    }

    public void setNatureofapplication(String natureofapplication) {
        this.natureofapplication = natureofapplication;
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

    public String getReferenceofId() {
        return referenceofId;
    }

    public void setReferenceofId(String referenceofId) {
        this.referenceofId = referenceofId;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getMaritialStatus() {
        return maritialStatus;
    }

    public void setMaritialStatus(String maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public String getNatureofpower() {
        return natureofpower;
    }

    public void setNatureofpower(String natureofpower) {
        this.natureofpower = natureofpower;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getIssuancedate() {
        return issuancedate;
    }

    public void setIssuancedate(Date issuancedate) {
        this.issuancedate = issuancedate;
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

    public String getNeighbour_south() {
        return neighbour_south;
    }

    public void setNeighbour_south(String neighbour_south) {
        this.neighbour_south = neighbour_south;
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

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getIdcardestablishment_date() {
        return idcardestablishment_date;
    }

    public void setIdcardestablishment_date(Date idcardestablishment_date) {
        this.idcardestablishment_date = idcardestablishment_date;
    }

    public String getIdcard_origin() {
        return idcard_origin;
    }

    public void setIdcard_origin(String idcard_origin) {
        this.idcard_origin = idcard_origin;
    }

    public String getMandate_location() {
        return mandate_location;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getMandate_establishmentDate() {
        return mandate_establishmentDate;
    }

    public void setMandate_establishmentDate(Date mandate_establishmentDate) {
        this.mandate_establishmentDate = mandate_establishmentDate;
    }

    public void setMandate_location(String mandate_location) {
        this.mandate_location = mandate_location;
    }

    public String getCfvname() {
        return cfvname;
    }

    public void setCfvname(String cfvname) {
        this.cfvname = cfvname;
    }

    public int getTenancyId() {
        return tenancyId;
    }

    public void setTenancyId(int tenancyId) {
        this.tenancyId = tenancyId;
    }

}
