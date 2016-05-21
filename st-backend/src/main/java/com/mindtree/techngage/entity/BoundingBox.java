package com.mindtree.techngage.entity;

/**
 * This class defines a bounding box or a rectangle to be plotted.
 * It contains the minX,maxX,minY and maxY values.
 * Created by tejas0908 on 20/05/16.
 */
public class BoundingBox {
    private Double minX;
    private Double minY;
    private Double maxX;
    private Double maxY;

    public Double getMaxY() {
        return maxY;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public Double getMaxX() {
        return maxX;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMinY() {
        return minY;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMinX() {
        return minX;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public BoundingBox(Double minX, Double minY, Double maxX, Double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }
}
