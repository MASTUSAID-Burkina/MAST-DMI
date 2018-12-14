package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApfrStatSummary implements Serializable {
    @Id
    private String name;
    @Column
    private Double males;
    @Column
    private Double females;
    @Column
    private Double collective;
    
    public ApfrStatSummary(){
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMales() {
        return males;
    }

    public void setMales(Double males) {
        this.males = males;
    }

    public Double getFemales() {
        return females;
    }

    public void setFemales(Double females) {
        this.females = females;
    }

    public Double getCollective() {
        return collective;
    }

    public void setCollective(Double collective) {
        this.collective = collective;
    }
}


