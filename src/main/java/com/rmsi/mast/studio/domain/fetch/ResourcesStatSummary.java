package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ResourcesStatSummary implements Serializable {
    @Id
    private Double total;
    @Column
    private Double chartered;
    @Column
    private Double adopted;
    
    public ResourcesStatSummary(){
        
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getChartered() {
        return chartered;
    }

    public void setChartered(Double chartered) {
        this.chartered = chartered;
    }

    public Double getAdopted() {
        return adopted;
    }

    public void setAdopted(Double adopted) {
        this.adopted = adopted;
    }
}


