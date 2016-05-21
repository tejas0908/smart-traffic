package com.mindtree.techngage.services;

import com.mindtree.techngage.entity.SignalTimes;

/**
 * This interface defines the method to be implemented by a signal time calculator.
 * Created by tejas0908 on 21/05/16.
 */
public interface SignalTimeCalculator {
    SignalTimes calculateSignalTimes(SignalTimes signalTimes) throws Exception;
}
