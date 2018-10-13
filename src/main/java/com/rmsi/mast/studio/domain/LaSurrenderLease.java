package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "la_surrenderlease")
public class LaSurrenderLease implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "pk_la_surrenderlease", sequenceName = "la_surrenderlease_leaseid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_la_surrenderlease")
    private Integer leaseid;

    private Integer years;
    private Integer months;

    private Double leaseamount;

    private Boolean isactive;

    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    private Integer modifiedby;

    @Temporal(TemporalType.DATE)
    private Date modifieddate;

    private Long personid;
    private Long ownerid;
    private String surrenderreason;
    private Long landid;

    @Temporal(TemporalType.DATE)
    private Date leasestartdate;

    @Temporal(TemporalType.DATE)
    private Date leaseenddate;

    public Integer getLeaseid() {
        return leaseid;
    }

    public void setLeaseid(Integer leaseid) {
        this.leaseid = leaseid;
    }

    public Double getLeaseamount() {
        return leaseamount;
    }

    public void setLeaseamount(Double leaseamount) {
        this.leaseamount = leaseamount;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Integer getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(Integer modifiedby) {
        this.modifiedby = modifiedby;
    }

    public Date getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(Date modifieddate) {
        this.modifieddate = modifieddate;
    }

    public Long getPersonid() {
        return personid;
    }

    public void setPersonid(Long personid) {
        this.personid = personid;
    }

    public Long getLandid() {
        return landid;
    }

    public void setLandid(Long landid) {
        this.landid = landid;
    }

    public Long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Long ownerid) {
        this.ownerid = ownerid;
    }

    public Date getLeasestartdate() {
        return leasestartdate;
    }

    public void setLeasestartdate(Date leasestartdate) {
        this.leasestartdate = leasestartdate;
    }

    public Date getLeaseenddate() {
        return leaseenddate;
    }

    public void setLeaseenddate(Date leaseenddate) {
        this.leaseenddate = leaseenddate;
    }

    public String getSurrenderreason() {
        return surrenderreason;
    }

    public void setSurrenderreason(String surrenderreason) {
        this.surrenderreason = surrenderreason;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

}
