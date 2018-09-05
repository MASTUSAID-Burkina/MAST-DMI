package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Returns person of interest from the view
 */
@Entity
@Table(name = "la_ext_spatialunit_personwithinterest")
public class PoiBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "last_name")
    private String lastName;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dob;
    
    @Column(name = "gender")
    private Integer genderId;

    @Column(name="relation")
    private Integer relationshipTypeId;

    @Column(name = "address")
    private String address;
    
    @Column(name = "identityno")
    private String idNumber;
    
    @ManyToOne
    @JoinColumn(name = "landid", insertable = false, updatable = false)
    private ClaimBasic laSpatialunitLand;
    
    public PoiBasic(){
        
    }

    public ClaimBasic getLaSpatialunitLand() {
        return laSpatialunitLand;
    }

    public void setLaSpatialunitLand(ClaimBasic laSpatialunitLand) {
        this.laSpatialunitLand = laSpatialunitLand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
    }
}
