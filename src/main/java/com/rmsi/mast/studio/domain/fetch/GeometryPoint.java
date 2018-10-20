package com.rmsi.mast.studio.domain.fetch;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeometryPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long index;
    private double x;
    private double y;

    public GeometryPoint() {
        super();
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
