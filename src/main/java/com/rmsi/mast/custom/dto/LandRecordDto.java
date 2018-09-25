package com.rmsi.mast.custom.dto;

import com.rmsi.mast.studio.domain.ClaimType;
import java.io.Serializable;

import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.Workflow;

public class LandRecordDto implements Serializable {

    private String application_no;

    private String pv_no;

    private String firstname;

    private String lastname;

    private ClaimType parceltype;

    private ShareType application_type;

    private Workflow workflow_stage;

    private Status application_stage;

    private long usin;

    private String apfr_no;

    private Integer section;

    private String project;

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    public String getPv_no() {
        return pv_no;
    }

    public void setPv_no(String pv_no) {
        this.pv_no = pv_no;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ClaimType getParceltype() {
        return parceltype;
    }

    public void setParceltype(ClaimType parceltype) {
        this.parceltype = parceltype;
    }

    public ShareType getApplication_type() {
        return application_type;
    }

    public void setApplication_type(ShareType application_type) {
        this.application_type = application_type;
    }

    public Workflow getWorkflow_stage() {
        return workflow_stage;
    }

    public void setWorkflow_stage(Workflow workflow_stage) {
        this.workflow_stage = workflow_stage;
    }

    public Status getApplication_stage() {
        return application_stage;
    }

    public void setApplication_stage(Status application_stage) {
        this.application_stage = application_stage;
    }

    public long getUsin() {
        return usin;
    }

    public void setUsin(long usin) {
        this.usin = usin;
    }

    public String getApfr_no() {
        return apfr_no;
    }

    public void setApfr_no(String apfr_no) {
        this.apfr_no = apfr_no;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}
