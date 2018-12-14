package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ResourceDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer landid;
    private String classificationName;
    private String subclassificationName;
    private String categoryName;
    private String geometryName;
    private String projectName;
    private String personName;
    private Integer projectId;
    private Boolean chartered;
    private String comment;
    private Boolean validatedByCouncil;
    private Date validationDate;
    private Boolean inExploitation;
    
    public Integer getLandid() {
        return landid;
    }

    public void setLandid(Integer landid) {
        this.landid = landid;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getSubclassificationName() {
        return subclassificationName;
    }

    public void setSubclassificationName(String subclassificationName) {
        this.subclassificationName = subclassificationName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGeometryName() {
        return geometryName;
    }

    public void setGeometryName(String geometryName) {
        this.geometryName = geometryName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Boolean getChartered() {
        return chartered;
    }

    public void setChartered(Boolean chartered) {
        this.chartered = chartered;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getValidatedByCouncil() {
        return validatedByCouncil;
    }

    public void setValidatedByCouncil(Boolean validatedByCouncil) {
        this.validatedByCouncil = validatedByCouncil;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public Boolean getInExploitation() {
        return inExploitation;
    }

    public void setInExploitation(Boolean inExploitation) {
        this.inExploitation = inExploitation;
    }

}
