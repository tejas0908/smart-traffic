package com.mindtree.techngage.entity;

import java.io.Serializable;

/**
 * This class defines the road ping structure.
 * Created by tejas0908 on 28/04/16.
 */
public class RoadPing implements Serializable {
    private Integer roadId;

    public Integer getRoadId() {
        return roadId;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public RoadPing(Integer roadId) {
        this.roadId = roadId;
    }

    public RoadPing() {
    }

    @Override
    public String toString() {
        return "RoadPing{" +
                "roadId=" + roadId +
                '}';
    }
}
