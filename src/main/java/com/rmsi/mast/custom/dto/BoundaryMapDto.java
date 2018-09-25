package com.rmsi.mast.custom.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rmsi.mast.studio.util.JsonDateSerializer2;

public class BoundaryMapDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String villagename;
    private String application_no;
    private Date applicationdate;
    private List<String> neighbourLst;
    private String sfr_name;
    private String region;
    private String province;
    private String commune;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillagename() {
        return villagename;
    }

    public void setVillagename(String villagename) {
        this.villagename = villagename;
    }

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    @JsonSerialize(using = JsonDateSerializer2.class)
    public Date getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(Date applicationdate) {
        this.applicationdate = applicationdate;
    }

    public List<String> getNeighbourLst() {
        return neighbourLst;
    }

    public void setNeighbourLst(List<String> neighbourLst) {
        this.neighbourLst = neighbourLst;
    }

    public String getSfr_name() {
        return sfr_name;
    }

    public void setSfr_name(String sfr_name) {
        this.sfr_name = sfr_name;
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

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

}
