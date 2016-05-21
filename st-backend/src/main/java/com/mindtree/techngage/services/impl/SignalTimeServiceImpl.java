package com.mindtree.techngage.services.impl;

import com.mindtree.techngage.entity.SignalTimes;
import com.mindtree.techngage.services.SignalTimeCalculator;
import com.mindtree.techngage.services.SignalTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of signal time service.
 * Created by tejas0908 on 21/05/16.
 */
@Component
public class SignalTimeServiceImpl implements SignalTimeService {

    @Autowired
    private SignalTimeCalculator calculator;

    @Override
    public SignalTimes calculateSignalTimes(SignalTimes signalTimes) throws Exception {
        return calculator.calculateSignalTimes(signalTimes);
    }
}
