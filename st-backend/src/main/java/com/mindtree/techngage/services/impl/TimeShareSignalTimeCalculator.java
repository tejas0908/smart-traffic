package com.mindtree.techngage.services.impl;

import com.mindtree.techngage.entity.SignalTimes;
import com.mindtree.techngage.services.SignalTimeCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This implementation assumes that total available time is fixed. eg 400 seconds , etc.
 * It proceeds to split this time into 4 parts based on the vehicle count.
 * Created by tejas0908 on 21/05/16.
 */
@Component
public class TimeShareSignalTimeCalculator implements SignalTimeCalculator {

    final static Logger LOGGER = LoggerFactory.getLogger(TimeShareSignalTimeCalculator.class);

    @Value("${time.share.total}")
    private Integer TOTAL_TIME;

    @Override
    public SignalTimes calculateSignalTimes(SignalTimes signalTimes) throws Exception {
        //get list of vehicle counts
        List<Integer> vehicleCounts = signalTimes.getSignalTimes().stream().map(signalTime -> {
            return signalTime.getVehicleCount();
        }).collect(Collectors.toList());

        //calculate percentages
        Integer totalCount = vehicleCounts.stream().mapToInt(i -> i.intValue()).sum();

        List<Long> times = vehicleCounts.stream().map(i -> {
            double y = (i / totalCount.doubleValue());
            double z = y * TOTAL_TIME;
            long x = Math.round(z);
            return x;
        }).collect(Collectors.toList());

        signalTimes.setSignalTimes(signalTimes.getSignalTimes().stream().map(signalTime -> {
            signalTime.setTime(times.get(signalTime.getRoadId() / 2 - 1).intValue());
            return signalTime;
        }).collect(Collectors.toList()));
        return signalTimes;
    }
}
