package com.mindtree.techngage.entity;

import java.util.List;

/**
 * This class defines signal times.
 * Created by tejas0908 on 20/05/16.
 */
public class SignalTime {
    private Integer roadId;
    private Integer vehicleCount;
    private Integer time;
    private List<BoundingBox> boxes;

    public Integer getRoadId() {
        return roadId;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public Integer getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(Integer vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<BoundingBox> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<BoundingBox> boxes) {
        this.boxes = boxes;
    }

    @Override
    public String toString() {
        return "SignalTime{" +
                "roadId=" + roadId +
                ", vehicleCount=" + vehicleCount +
                ", time=" + time +
                ", boxes=" + boxes +
                '}';
    }
}
