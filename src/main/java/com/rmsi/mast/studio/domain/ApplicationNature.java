package com.rmsi.mast.studio.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "nature_of_application")
public class ApplicationNature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "noa_id")
    private Integer id;

    @Column(name = "nature_of_application_fr")
    private String name;

    @Column(name = "nature_of_application")
    private String nameEn;

    public ApplicationNature() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
