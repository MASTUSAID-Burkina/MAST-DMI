package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Entity implementation class for Entity: NonNaturalPerson
 *
 * @author Shruti.Thakur
 */
@Entity
@Table(name = "la_party_organization")
@PrimaryKeyJoinColumn(name = "ORGANIZATIONID", referencedColumnName = "partyid")
public class NonNaturalPerson extends LaParty implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "contactno")
    private String contactno;

    @ManyToOne
    @JoinColumn(name = "grouptypeid")
    private GroupType groupType;

    @Column(name = "createdby")
    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    @Column(name = "emailid")
    private String emailid;

    @Column(name = "faxno")
    private String faxno;

    @Column(name = "identityregistrationno")
    private String identityregistrationno;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "modifiedby")
    private Integer modifiedby;

    @Temporal(TemporalType.DATE)
    private Date modifieddate;

    @Column(name = "organizationname")
    private String organizationname;

    @Column(name = "website")
    private String website;

    @Column
    private String repname;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date regdate;
    
    private String address;

    @Transient
    private String persontype;
    
    @ManyToOne
    @JoinColumn(name = "identitytypeid")
    private IdType laPartygroupIdentitytype;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid1")
    private LaSpatialunitgroup laSpatialunitgroup1;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid2")
    private LaSpatialunitgroup laSpatialunitgroup2;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid3")
    private LaSpatialunitgroup laSpatialunitgroup3;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid4")
    private LaSpatialunitgroup laSpatialunitgroup4;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid5")
    private LaSpatialunitgroup laSpatialunitgroup5;

    //bi-directional many-to-one association to LaSpatialunitgroup
    @ManyToOne
    @JoinColumn(name = "spatialunitgroupid6")
    private LaSpatialunitgroup laSpatialunitgroup6;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid1")
    private ProjectRegion laSpatialunitgroupHierarchy1;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid2")
    private ProjectRegion laSpatialunitgroupHierarchy2;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid3")
    private ProjectRegion laSpatialunitgroupHierarchy3;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid4")
    private ProjectRegion laSpatialunitgroupHierarchy4;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid5")
    private ProjectRegion laSpatialunitgroupHierarchy5;

    //bi-directional many-to-one association to LaSpatialunitgroupHierarchy
    @ManyToOne
    @JoinColumn(name = "hierarchyid6")
    private ProjectRegion laSpatialunitgroupHierarchy6;

    public NonNaturalPerson() {

    }

    public String getPersontype() {
        return persontype;
    }

    public void setPersontype(String persontype) {
        this.persontype = persontype;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public String getRepname() {
        return repname;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public void setRepname(String repname) {
        this.repname = repname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
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

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getFaxno() {
        return faxno;
    }

    public void setFaxno(String faxno) {
        this.faxno = faxno;
    }

    public String getIdentityregistrationno() {
        return identityregistrationno;
    }

    public void setIdentityregistrationno(String identityregistrationno) {
        this.identityregistrationno = identityregistrationno;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
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

    public String getOrganizationname() {
        return organizationname;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /*public LaParty getLaParty() {
		return laParty;
	}

	public void setLaParty(LaParty laParty) {
		this.laParty = laParty;
	}*/
    public IdType getLaPartygroupIdentitytype() {
        return laPartygroupIdentitytype;
    }

    public void setLaPartygroupIdentitytype(IdType laPartygroupIdentitytype) {
        this.laPartygroupIdentitytype = laPartygroupIdentitytype;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup1() {
        return laSpatialunitgroup1;
    }

    public void setLaSpatialunitgroup1(LaSpatialunitgroup laSpatialunitgroup1) {
        this.laSpatialunitgroup1 = laSpatialunitgroup1;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup2() {
        return laSpatialunitgroup2;
    }

    public void setLaSpatialunitgroup2(LaSpatialunitgroup laSpatialunitgroup2) {
        this.laSpatialunitgroup2 = laSpatialunitgroup2;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup3() {
        return laSpatialunitgroup3;
    }

    public void setLaSpatialunitgroup3(LaSpatialunitgroup laSpatialunitgroup3) {
        this.laSpatialunitgroup3 = laSpatialunitgroup3;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup4() {
        return laSpatialunitgroup4;
    }

    public void setLaSpatialunitgroup4(LaSpatialunitgroup laSpatialunitgroup4) {
        this.laSpatialunitgroup4 = laSpatialunitgroup4;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup5() {
        return laSpatialunitgroup5;
    }

    public void setLaSpatialunitgroup5(LaSpatialunitgroup laSpatialunitgroup5) {
        this.laSpatialunitgroup5 = laSpatialunitgroup5;
    }

    public LaSpatialunitgroup getLaSpatialunitgroup6() {
        return laSpatialunitgroup6;
    }

    public void setLaSpatialunitgroup6(LaSpatialunitgroup laSpatialunitgroup6) {
        this.laSpatialunitgroup6 = laSpatialunitgroup6;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy1() {
        return laSpatialunitgroupHierarchy1;
    }

    public void setLaSpatialunitgroupHierarchy1(
            ProjectRegion laSpatialunitgroupHierarchy1) {
        this.laSpatialunitgroupHierarchy1 = laSpatialunitgroupHierarchy1;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy2() {
        return laSpatialunitgroupHierarchy2;
    }

    public void setLaSpatialunitgroupHierarchy2(
            ProjectRegion laSpatialunitgroupHierarchy2) {
        this.laSpatialunitgroupHierarchy2 = laSpatialunitgroupHierarchy2;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy3() {
        return laSpatialunitgroupHierarchy3;
    }

    public void setLaSpatialunitgroupHierarchy3(
            ProjectRegion laSpatialunitgroupHierarchy3) {
        this.laSpatialunitgroupHierarchy3 = laSpatialunitgroupHierarchy3;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy4() {
        return laSpatialunitgroupHierarchy4;
    }

    public void setLaSpatialunitgroupHierarchy4(
            ProjectRegion laSpatialunitgroupHierarchy4) {
        this.laSpatialunitgroupHierarchy4 = laSpatialunitgroupHierarchy4;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy5() {
        return laSpatialunitgroupHierarchy5;
    }

    public void setLaSpatialunitgroupHierarchy5(
            ProjectRegion laSpatialunitgroupHierarchy5) {
        this.laSpatialunitgroupHierarchy5 = laSpatialunitgroupHierarchy5;
    }

    public ProjectRegion getLaSpatialunitgroupHierarchy6() {
        return laSpatialunitgroupHierarchy6;
    }

    public void setLaSpatialunitgroupHierarchy6(
            ProjectRegion laSpatialunitgroupHierarchy6) {
        this.laSpatialunitgroupHierarchy6 = laSpatialunitgroupHierarchy6;
    }

}
