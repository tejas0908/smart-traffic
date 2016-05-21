package com.mindtree.techngage.entity;

import java.util.List;

/**
 * This class defined the machine learning item response.
 * Created by tejas0908 on 20/05/16.
 */
public class MLItem {
    private List<Double> bbox;
    private String category;
    private Double confidence;

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "MLItem{" +
                "bbox=" + bbox +
                ", category='" + category + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
