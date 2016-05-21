package com.mindtree.techngage.entity;

import java.util.List;

/**
 * This class is the aggregate of all signal times.
 * Created by tejas0908 on 20/05/16.
 */
public class SignalTimes {
    List<SignalTime> signalTimes;

    public List<SignalTime> getSignalTimes() {
        return signalTimes;
    }

    public void setSignalTimes(List<SignalTime> signalTimes) {
        this.signalTimes = signalTimes;
    }

    @Override
    public String toString() {
        return "SignalTimes{" +
                "signalTimes=" + signalTimes +
                '}';
    }
}
