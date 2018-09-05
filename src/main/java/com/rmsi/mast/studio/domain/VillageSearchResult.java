package com.rmsi.mast.studio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VillageSearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "hierarchyid")
    private Integer id;

    @Column(name = "isactive")
    private Boolean active;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "uperhierarchyid")
    private Integer communeId;
    
    @Column(name = "commune_name")
    private String communeName;
    
    @Column(name = "commune_name_en")
    private String communeNameEn;

    @Column(name = "province_id")
    private Integer provinceId;
    
    @Column(name = "province_name")
    private String provinceName;
    
    @Column(name = "province_name_en")
    private String provinceNameEn;
    
    @Column
    private String code;

    @Column(name = "cfv_agent")
    private String cfvAgent;
    
    public VillageSearchResult() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    public String getCommuneNameEn() {
        return communeNameEn;
    }

    public void setCommuneNameEn(String communeNameEn) {
        this.communeNameEn = communeNameEn;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceNameEn() {
        return provinceNameEn;
    }

    public void setProvinceNameEn(String provinceNameEn) {
        this.provinceNameEn = provinceNameEn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCfvAgent() {
        return cfvAgent;
    }

    public void setCfvAgent(String cfvAgent) {
        this.cfvAgent = cfvAgent;
    }

    public Integer getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Integer communeId) {
        this.communeId = communeId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
}
