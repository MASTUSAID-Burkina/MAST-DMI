package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Returns only project id and name
 *
 */
@Entity
@Table(name = "la_spatialsource_projectname")
public class ProjectName implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "projectnameid")
    private Long projectnameid;

    @Column(name = "projectname")
    private String name;

    public ProjectName() {
    }

    public Long getProjectnameid() {
        return projectnameid;
    }

    public void setProjectnameid(Long projectnameid) {
        this.projectnameid = projectnameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
