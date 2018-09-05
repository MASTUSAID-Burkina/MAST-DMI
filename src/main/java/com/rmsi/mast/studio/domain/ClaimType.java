package com.rmsi.mast.studio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "la_right_claimtype")

public class ClaimType implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // private static final long serialVersionUID = 1L;
    public static final String CODE_NEW = "newClaim";
    public static final String CODE_EXISTING = "existingClaim";
    public static final String CODE_DISPUTED = "dispute";
    public static final String CODE_UNCLAIMED = "unclaimed";

    @Id
    @Column(name = "claimtypeid")
    private String code;
    @Column(name = "claimtype_en")
    private String name;

    @Column(name = "claimtype")
    private String nameOtherLang;
    @Column(name = "isactive")
    boolean active;

    public ClaimType() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOtherLang() {
        return nameOtherLang;
    }

    public void setNameOtherLang(String nameOtherLang) {
        this.nameOtherLang = nameOtherLang;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
