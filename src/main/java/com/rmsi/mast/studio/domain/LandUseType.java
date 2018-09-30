package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: LandUseType
 *
 */
@Entity
@Table(name = "la_baunit_landusetype")
public class LandUseType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer landusetypeid;

    private Boolean isactive;

    private String landusetype;

    @Column(name = "landusetype_en")
    private String landusetypeEn;

    public LandUseType() {
        super();
    }

    public Integer getLandusetypeid() {
        return landusetypeid;
    }

    public void setLandusetypeid(Integer landusetypeid) {
        this.landusetypeid = landusetypeid;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getLandusetype() {
        return landusetype;
    }

    public void setLandusetype(String landusetype) {
        this.landusetype = landusetype;
    }

    public String getLandusetypeEn() {
        return landusetypeEn;
    }

    public void setLandusetypeEn(String landusetypeEn) {
        this.landusetypeEn = landusetypeEn;
    }
}
