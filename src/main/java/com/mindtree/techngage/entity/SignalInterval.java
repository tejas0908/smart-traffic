package com.mindtree.techngage.entity;

/**
 * Created by tejas0908 on 28/04/16.
 */
public class SignalInterval {
    private Integer roadId;
    private Long interval;
    private Integer congestionCount;

    public Integer getCongestionCount() {
        return congestionCount;
    }

    public void setCongestionCount(Integer congestionCount) {
        this.congestionCount = congestionCount;
    }

    public Integer getRoadId() {
        return roadId;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "SignalInterval{" +
                "roadId=" + roadId +
                ", interval=" + interval +
                '}';
    }
}
