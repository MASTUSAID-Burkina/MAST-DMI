package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionsStatSummary implements Serializable {
    @Id
    private String name;
    @Column
    private int males;
    @Column(name = "males_area")
    private Double malesArea;
    @Column
    private int females;
    @Column(name = "females_area")
    private Double femalesArea;
    @Column
    private int collective;
    @Column(name = "collective_area")
    private Double collectiveArea;
    
    public TransactionsStatSummary(){
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMales() {
        return males;
    }

    public void setMales(int males) {
        this.males = males;
    }

    public Double getMalesArea() {
        return malesArea;
    }

    public void setMalesArea(Double malesArea) {
        this.malesArea = malesArea;
    }

    public int getFemales() {
        return females;
    }

    public void setFemales(int females) {
        this.females = females;
    }

    public Double getFemalesArea() {
        return femalesArea;
    }

    public void setFemalesArea(Double femalesArea) {
        this.femalesArea = femalesArea;
    }

    public int getCollective() {
        return collective;
    }

    public void setCollective(int collective) {
        this.collective = collective;
    }

    public Double getCollectiveArea() {
        return collectiveArea;
    }

    public void setCollectiveArea(Double collectiveArea) {
        this.collectiveArea = collectiveArea;
    }
}


