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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rmsi.mast.studio.domain.LaParty;

@Entity
@Table(name = "la_ext_personlandmapping")
public class RightBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personlandid")
    private Integer personlandid;

    @Column(name = "createdby")
    private Integer createdby;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "modifiedby")
    private Integer modifiedby;

    @Temporal(TemporalType.DATE)
    private Date modifieddate;

    @Column(name = "certificateno")
    private String certNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "certificateissuedate")
    private Date certIssueDate;

    @Column(name = "certificatetypeid")
    private Integer certTypeid;

    //bi-directional many-to-one association to LaExtTransactiondetail
    @ManyToOne
    @JoinColumn(name = "transactionid")
    private LaExtTransactiondetailBasic laExtTransactiondetail;

    //bi-directional many-to-one association to LaParty
    @ManyToOne
    @JoinColumn(name = "partyid", insertable = false, updatable = false)
    private LaParty laParty;

    @Column(name = "persontypeid")
    private Integer persontypeId;

    @ManyToOne
    @JoinColumn(name = "landid", insertable = false, updatable = false)
    private ClaimBasic laSpatialunitLand;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parentuid")
    private List<RightAttributeValue> attributes;

    @Column(name = "mutation_id")
    private Integer mutationId;

    @Column(name = "transfer_contract_name")
    private String transferContractName;

    @Column(name = "transfer_contract_num")
    private String transferContractNum;

    @Column(name = "transfer_contract_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transferContractDate;

    @Column(name = "share_type_id")
    private Integer shareTypeId;
    
    public RightBasic() {
    }

    public List<RightAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<RightAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public Integer getPersonlandid() {
        return personlandid;
    }

    public void setPersonlandid(Integer personlandid) {
        this.personlandid = personlandid;
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

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public Date getCertIssueDate() {
        return certIssueDate;
    }

    public void setCertIssueDate(Date certIssueDate) {
        this.certIssueDate = certIssueDate;
    }

    public Integer getCertTypeid() {
        return certTypeid;
    }

    public void setCertTypeid(Integer certTypeid) {
        this.certTypeid = certTypeid;
    }

    public LaExtTransactiondetailBasic getLaExtTransactiondetail() {
        return laExtTransactiondetail;
    }

    public void setLaExtTransactiondetail(
            LaExtTransactiondetailBasic laExtTransactiondetail) {
        this.laExtTransactiondetail = laExtTransactiondetail;
    }

    public LaParty getLaParty() {
        return laParty;
    }

    public void setLaParty(LaParty laParty) {
        this.laParty = laParty;
    }

    public Integer getPersontypeId() {
        return persontypeId;
    }

    public void setPersontypeId(Integer persontypeId) {
        this.persontypeId = persontypeId;
    }

    @JsonIgnore
    public ClaimBasic getLaSpatialunitLand() {
        return laSpatialunitLand;
    }

    public void setLaSpatialunitLand(ClaimBasic laSpatialunitLand) {
        this.laSpatialunitLand = laSpatialunitLand;
    }

    public Integer getMutationId() {
        return mutationId;
    }

    public void setMutationId(Integer mutationId) {
        this.mutationId = mutationId;
    }

    public String getTransferContractName() {
        return transferContractName;
    }

    public void setTransferContractName(String transferContractName) {
        this.transferContractName = transferContractName;
    }

    public String getTransferContractNum() {
        return transferContractNum;
    }

    public void setTransferContractNum(String transferContractNum) {
        this.transferContractNum = transferContractNum;
    }

    public Date getTransferContractDate() {
        return transferContractDate;
    }

    public void setTransferContractDate(Date transferContractDate) {
        this.transferContractDate = transferContractDate;
    }

    public Integer getShareTypeId() {
        return shareTypeId;
    }

    public void setShareTypeId(Integer shareTypeId) {
        this.shareTypeId = shareTypeId;
    }
}
