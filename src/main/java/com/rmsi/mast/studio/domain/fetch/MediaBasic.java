package com.rmsi.mast.studio.domain.fetch;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rmsi.mast.studio.domain.LaParty;
import com.rmsi.mast.studio.domain.Outputformat;

@Entity
@Table(name = "la_ext_documentdetails")
public class MediaBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "documentid")
    private Integer documentid;

    @Column(name = "createdby")
    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    @Column(name = "documentlocation")
    private String documentlocation;

    @Column(name = "documentname")
    private String documentname;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "modifiedby")
    private Integer modifiedby;

    @Temporal(TemporalType.DATE)
    private Date modifieddate;

    //bi-directional many-to-one association to LaExtDocumentformat
    @ManyToOne
    @JoinColumn(name = "documentformatid")
    private Outputformat laExtDocumentformat;

    //bi-directional many-to-one association to LaExtTransactiondetail
    @ManyToOne
    @JoinColumn(name = "transactionid")
    private LaExtTransactiondetailBasic laExtTransactiondetail;

    //bi-directional many-to-one association to LaParty
    @ManyToOne
    @JoinColumn(name = "partyid", insertable = false, updatable = false)
    private LaParty laParty;

    @Column
    private Long landid;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parentuid")
    private List<MediaAttributeValue> attributes;

    public MediaBasic() {
//       super();
    }

    public LaExtTransactiondetailBasic getLaExtTransactiondetail() {
        return laExtTransactiondetail;
    }

    public void setLaExtTransactiondetail(
            LaExtTransactiondetailBasic laExtTransactiondetail) {
        this.laExtTransactiondetail = laExtTransactiondetail;
    }

    public Integer getDocumentid() {
        return documentid;
    }

    public void setDocumentid(Integer documentid) {
        this.documentid = documentid;
    }

    public Long getLandid() {
        return landid;
    }

    public void setLandid(Long landid) {
        this.landid = landid;
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

    public String getDocumentlocation() {
        return documentlocation;
    }

    public void setDocumentlocation(String documentlocation) {
        this.documentlocation = documentlocation;
    }

    public String getDocumentname() {
        return documentname;
    }

    public void setDocumentname(String documentname) {
        this.documentname = documentname;
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

    public Outputformat getLaExtDocumentformat() {
        return laExtDocumentformat;
    }

    public void setLaExtDocumentformat(Outputformat laExtDocumentformat) {
        this.laExtDocumentformat = laExtDocumentformat;
    }

//	public LaExtTransactiondetail getLaExtTransactiondetail() {
//		return laExtTransactiondetail;
//	}
//
//	public void setLaExtTransactiondetail(
//			LaExtTransactiondetail laExtTransactiondetail) {
//		this.laExtTransactiondetail = laExtTransactiondetail;
//	}
    public LaParty getLaParty() {
        return laParty;
    }

    public void setLaParty(LaParty laParty) {
        this.laParty = laParty;
    }

    public List<MediaAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MediaAttributeValue> attributes) {
        this.attributes = attributes;
    }
}
