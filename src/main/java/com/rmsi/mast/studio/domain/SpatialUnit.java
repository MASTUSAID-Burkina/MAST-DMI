package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Entity implementation class for Entity: SpatialUnit
 *
 */
@Entity
@Table(name = "la_spatialunit_land")
public class SpatialUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "pk_la_spatialunit_land", sequenceName = "la_spatialunit_land_landid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_la_spatialunit_land")
    private Long landid;

    @Column(name = "ogc_fid")
    private Integer ogc_fid;

    @Column(name = "area")
    private double area;

    @Column(name = "createdby")
    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    @Column(name = "geometry", columnDefinition = "geometry(Geometry,4326)")
    private Geometry geometry;

    @Column(name = "geometrytype")
    private String geometrytype;

    @Column
    private String imei;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "landno")
    private String landno;

    @Column(name = "modifiedby")
    private Integer modifiedby;

    @Temporal(TemporalType.DATE)
    private Date modifieddate;

    @Column(name = "neighbor_east")
    private String neighborEast;

    @Column(name = "neighbor_north")
    private String neighborNorth;

    @Column(name = "neighbor_south")
    private String neighborSouth;

    @Column(name = "neighbor_west")
    private String neighborWest;

    private Integer applicationstatusid;

    private Integer workflowstatusid;

    private Integer projectnameid;

    @Temporal(TemporalType.DATE)
    private Date surveydate;

    private String other_use;

    @Column(name = "application_no")
    private String appNum;

    @Column(name = "pv_no")
    private String pvNum;

    @Column(name = "apfr_no")
    private String apfrNum;
    
    @Column
    private Integer section;
    
    @Column(name = "parcel_no_in_section")
    private Long parcelNoInSection;
    
    @Column(name = "noa_id")
    private Integer noaId;
    
    @Column(name = "parcelno")
    private String parcelNum;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date applicationdate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date issuancedate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "public_notice_startdate")
    private Date publicNoticeStartDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "public_notice_enddate")
    private Date publicNoticeEndDate;
    
    @Column(name = "registrationno")
    private String registrationNum;
    
    public String getOther_use() {
        return other_use;
    }

    public void setOther_use(String other_use) {
        this.other_use = other_use;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "landid")
    private List<SourceDocument> laExtDocumentdetails;

    //bi-directional many-to-one association to LaBaunitLandsoilquality
    @ManyToOne
    @JoinColumn(name = "landsoilqualityid")
    private SoilQualityValues laBaunitLandsoilquality;

    //bi-directional many-to-one association to LaBaunitLandtype
    @ManyToOne
    @JoinColumn(name = "landtypeid")
    private LandType laBaunitLandtype;

    @Column
    private String landusetypeid;

    //bi-directional many-to-one association to LaExtSlopevalue
    @ManyToOne
    @JoinColumn(name = "slopevalueid")
    private SlopeValues laExtSlopevalue;

    //bi-directional many-to-one association to LaExtUnit
    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit laExtUnit;

    //bi-directional many-to-one association to LaRightAcquisitiontype
    @ManyToOne
    @JoinColumn(name = "acquisitiontypeid")
    private AcquisitionType laRightAcquisitiontype;

    @Column(name = "claimtypeid")
    private Integer claimtypeid;

    //bi-directional many-to-one association to LaRightLandsharetype
    @ManyToOne
    @JoinColumn(name = "landsharetypeid")
    private ShareType laRightLandsharetype;

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

    @Transient
    private String geomStr;

    private Integer proposedused;

    private Integer claimno;

    public Integer getTenureclassid() {
        return tenureclassid;
    }

    public void setTenureclassid(Integer tenureclassid) {
        this.tenureclassid = tenureclassid;
    }

    private Integer occupancylength;

    private Integer tenureclassid;

    public SpatialUnit() {

    }

    public Long getLandid() {
        return landid;
    }

    public Integer getOccupancylength() {
        return occupancylength;
    }

    public void setOccupancylength(Integer occupancylength) {
        this.occupancylength = occupancylength;
    }

    public void setLandid(Long landid) {
        this.landid = landid;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
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

    @JsonIgnore
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        geometry.setSRID(4326);
        this.geometry = geometry;
    }

    public String getGeometrytype() {
        return geometrytype;
    }

    public void setGeometrytype(String geometrytype) {
        this.geometrytype = geometrytype;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public Integer getProposedused() {
        return proposedused;
    }

    public void setProposedused(Integer proposedused) {
        this.proposedused = proposedused;
    }

    public Integer getClaimno() {
        return claimno;
    }

    public void setClaimno(Integer claimno) {
        this.claimno = claimno;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getLandno() {
        return landno;
    }

    public void setLandno(String landno) {
        this.landno = landno;
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

    public String getNeighborEast() {
        return neighborEast;
    }

    public void setNeighborEast(String neighborEast) {
        this.neighborEast = neighborEast;
    }

    public String getNeighborNorth() {
        return neighborNorth;
    }

    public void setNeighborNorth(String neighborNorth) {
        this.neighborNorth = neighborNorth;
    }

    public String getNeighborSouth() {
        return neighborSouth;
    }

    public void setNeighborSouth(String neighborSouth) {
        this.neighborSouth = neighborSouth;
    }

    public String getNeighborWest() {
        return neighborWest;
    }

    public void setNeighborWest(String neighborWest) {
        this.neighborWest = neighborWest;
    }

    public Integer getProjectnameid() {
        return projectnameid;
    }

    public void setProjectnameid(Integer projectnameid) {
        this.projectnameid = projectnameid;
    }

    public Date getSurveydate() {
        return surveydate;
    }

    public void setSurveydate(Date surveydate) {
        this.surveydate = surveydate;
    }

    public List<SourceDocument> getLaExtDocumentdetails() {
        return laExtDocumentdetails;
    }

    public void setLaExtDocumentdetails(List<SourceDocument> laExtDocumentdetails) {
        this.laExtDocumentdetails = laExtDocumentdetails;
    }

    public SoilQualityValues getLaBaunitLandsoilquality() {
        return laBaunitLandsoilquality;
    }

    public void setLaBaunitLandsoilquality(SoilQualityValues laBaunitLandsoilquality) {
        this.laBaunitLandsoilquality = laBaunitLandsoilquality;
    }

    public LandType getLaBaunitLandtype() {
        return laBaunitLandtype;
    }

    public void setLaBaunitLandtype(LandType laBaunitLandtype) {
        this.laBaunitLandtype = laBaunitLandtype;
    }

    public String getLandusetypeid() {
        return landusetypeid;
    }

    public void setLandusetypeid(String landusetypeid) {
        this.landusetypeid = landusetypeid;
    }

    public SlopeValues getLaExtSlopevalue() {
        return laExtSlopevalue;
    }

    public void setLaExtSlopevalue(SlopeValues laExtSlopevalue) {
        this.laExtSlopevalue = laExtSlopevalue;
    }

    public Unit getLaExtUnit() {
        return laExtUnit;
    }

    public void setLaExtUnit(Unit laExtUnit) {
        this.laExtUnit = laExtUnit;
    }

    public AcquisitionType getLaRightAcquisitiontype() {
        return laRightAcquisitiontype;
    }

    public void setLaRightAcquisitiontype(AcquisitionType laRightAcquisitiontype) {
        this.laRightAcquisitiontype = laRightAcquisitiontype;
    }

    public Integer getClaimtypeid() {
        return claimtypeid;
    }

    public void setClaimtypeid(Integer claimtypeid) {
        this.claimtypeid = claimtypeid;
    }

    public ShareType getLaRightLandsharetype() {
        return laRightLandsharetype;
    }

    public void setLaRightLandsharetype(ShareType laRightLandsharetype) {
        this.laRightLandsharetype = laRightLandsharetype;
    }

    /*public TenureClass getLaRightTenureclass() {
		return laRightTenureclass;
	}

	public void setLaRightTenureclass(TenureClass laRightTenureclass) {
		this.laRightTenureclass = laRightTenureclass;
	}*/
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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public Integer getApplicationstatusid() {
        return applicationstatusid;
    }

    public void setApplicationstatusid(Integer applicationstatusid) {
        this.applicationstatusid = applicationstatusid;
    }

    public Integer getWorkflowstatusid() {
        return workflowstatusid;
    }

    public void setWorkflowstatusid(Integer workflowstatusid) {
        this.workflowstatusid = workflowstatusid;
    }

    public String getGeomStr() {
        return geomStr;
    }

    public void setGeomStr(String geomStr) {
        this.geomStr = geomStr;
    }

    public String getAppNum() {
        return appNum;
    }

    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }

    public String getPvNum() {
        return pvNum;
    }

    public void setPvNum(String pvNum) {
        this.pvNum = pvNum;
    }

    public String getApfrNum() {
        return apfrNum;
    }

    public void setApfrNum(String apfrNum) {
        this.apfrNum = apfrNum;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Long getParcelNoInSection() {
        return parcelNoInSection;
    }

    public void setParcelNoInSection(Long parcelNoInSection) {
        this.parcelNoInSection = parcelNoInSection;
    }

    public Integer getNoaId() {
        return noaId;
    }

    public void setNoaId(Integer noaId) {
        this.noaId = noaId;
    }

    public String getParcelNum() {
        return parcelNum;
    }

    public void setParcelNum(String parcelNum) {
        this.parcelNum = parcelNum;
    }

    public Date getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(Date applicationdate) {
        this.applicationdate = applicationdate;
    }

    public Date getIssuancedate() {
        return issuancedate;
    }

    public void setIssuancedate(Date issuancedate) {
        this.issuancedate = issuancedate;
    }

    public Date getPublicNoticeStartDate() {
        return publicNoticeStartDate;
    }

    public void setPublicNoticeStartDate(Date publicNoticeStartDate) {
        this.publicNoticeStartDate = publicNoticeStartDate;
    }

    public Date getPublicNoticeEndDate() {
        return publicNoticeEndDate;
    }

    public void setPublicNoticeEndDate(Date publicNoticeEndDate) {
        this.publicNoticeEndDate = publicNoticeEndDate;
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    
    public Integer getOgc_fid() {
        return ogc_fid;
    }

    public void setOgc_fid(Integer ogc_fid) {
        this.ogc_fid = ogc_fid;
    }

}
