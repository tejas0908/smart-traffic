package com.mindtree.techngage.services;

import com.mindtree.techngage.entity.SignalTimes;

/**
 * The signal time service interface.
 * defines the calculate Signal Times method.
 * Created by tejas0908 on 21/05/16.
 */
public interface SignalTimeService {
    SignalTimes calculateSignalTimes(SignalTimes signalTimes) throws Exception;
}
