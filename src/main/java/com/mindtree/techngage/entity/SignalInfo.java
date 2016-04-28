package com.mindtree.techngage.entity;

import java.util.List;

/**
 * Created by tejas0908 on 28/04/16.
 */
public class SignalInfo {
    private List<SignalInterval> intervals;

    public SignalInfo(List<SignalInterval> intervals) {
        this.intervals = intervals;
    }

    public List<SignalInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<SignalInterval> intervals) {
        this.intervals = intervals;
    }

    @Override
    public String toString() {
        return "SignalInfo{" +
                "intervals=" + intervals +
                '}';
    }
}
