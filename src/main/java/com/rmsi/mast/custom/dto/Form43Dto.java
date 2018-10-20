package com.rmsi.mast.custom.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.domain.fetch.GeometryPoint;
import com.rmsi.mast.studio.util.JsonDateSerializer2;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Form43Dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region;
    private String province;
    private String commune;
    private String village;
    private String applicant;
    private String regNum;
    private Date electionDate;
    private Date appDate;
    private String appNum;
    private String usage;
    private Date idDate;
    private String idNum;
    private String address;
    private String location;
    private String lot;
    private int section;
    private long parcelNum;
    private String area;
    private String mayorName;
    private Date apfrDate;
    private String apfrNum;
    private Date startDate;
    private Date endDate;
    private List<GeometryPoint> coords;
    
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

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getElectionDate() {
        return electionDate;
    }

    public void setElectionDate(Date electionDate) {
        this.electionDate = electionDate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getAppNum() {
        return appNum;
    }

    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getIdDate() {
        return idDate;
    }

    public void setIdDate(Date idDate) {
        this.idDate = idDate;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public long getParcelNum() {
        return parcelNum;
    }

    public void setParcelNum(long parcelNum) {
        this.parcelNum = parcelNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMayorName() {
        return mayorName;
    }

    public void setMayorName(String mayorName) {
        this.mayorName = mayorName;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApfrDate() {
        return apfrDate;
    }

    public void setApfrDate(Date apfrDate) {
        this.apfrDate = apfrDate;
    }

    public String getApfrNum() {
        return apfrNum;
    }

    public void setApfrNum(String apfrNum) {
        this.apfrNum = apfrNum;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<GeometryPoint> getCoords() {
        return coords;
    }

    public void setCoords(List<GeometryPoint> coords) {
        this.coords = coords;
    }
}
