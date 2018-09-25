package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class PaymentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;
    private String commune;
    private String region;
    private String village;
    private String area;
    private String application_no;
    private Date applicationDate;
    private Date printDate;
    private String firstname;
    private String lastname;

    private String pv_no;
    private Date pv_date;

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public String getPv_no() {
        return pv_no;
    }

    public void setPv_no(String pv_no) {
        this.pv_no = pv_no;
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
    public Date getPv_date() {
        return pv_date;
    }

    public void setPv_date(Date pv_date) {
        this.pv_date = pv_date;
    }

}
