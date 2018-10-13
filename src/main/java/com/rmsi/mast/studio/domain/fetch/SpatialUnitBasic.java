package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.postgis.Geometry;


@Entity
@Table(name = "la_spatialunit_land")
public class SpatialUnitBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "landid")
    private Long landid;

    @Column(name = "area")
    private double area;

    @Column(name = "createdby")
    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    @Transient
    @Column(name = "geometry")
    private Geometry geometry;

    @Column(name = "geometrytype")
    private String geometrytype;

//	@Column(name="hierarchyid6")
//	private Integer hierarchyid6;
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

    @Column(name = "projectnameid")
    private Integer projectnameid;

    @Temporal(TemporalType.DATE)
    private Date surveydate;

    private String other_use;

    public String getOther_use() {
        return other_use;
    }

    public void setOther_use(String other_use) {
        this.other_use = other_use;
    }

    public SpatialUnitBasic() {

    }

    public Long getLandid() {
        return landid;
    }

    public void setProjectnameid(Integer projectnameid) {
        this.projectnameid = projectnameid;
    }

    public Integer getProjectnameid() {
        return projectnameid;
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

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
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

    public Date getSurveydate() {
        return surveydate;
    }

    public void setSurveydate(Date surveydate) {
        this.surveydate = surveydate;
    }
}
