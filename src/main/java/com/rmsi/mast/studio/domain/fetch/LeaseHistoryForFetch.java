package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the attribute database table.
 *
 */
@Entity
public class LeaseHistoryForFetch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long rnum;

    private Long landid;
    private String firstname;
    private String middlename;
    private String lastname;
    private String address;
    private String identityno;
    private Integer years;
    private Integer months;
    private Long leaseamount;
    private String conditions;

    @Temporal(TemporalType.DATE)
    private Date createddate;

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

    @Temporal(TemporalType.DATE)
    private Date leasestartdate;

    @Temporal(TemporalType.DATE)
    private Date leaseenddate;

    public Long getRnum() {
        return rnum;
    }

    public void setRnum(Long rnum) {
        this.rnum = rnum;
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

    public Long getLeaseamount() {
        return leaseamount;
    }

    public void setLeaseamount(Long leaseamount) {
        this.leaseamount = leaseamount;
    }

    public Long getLandid() {
        return landid;
    }

    public void setLandid(Long landid) {
        this.landid = landid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
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

    public String getIdentityno() {
        return identityno;
    }

    public void setIdentityno(String identityno) {
        this.identityno = identityno;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

}
