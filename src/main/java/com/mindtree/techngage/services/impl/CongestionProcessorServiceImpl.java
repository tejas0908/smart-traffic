package com.mindtree.techngage.services.impl;

import com.mindtree.techngage.entity.RoadPing;
import com.mindtree.techngage.entity.SignalInfo;
import com.mindtree.techngage.entity.SignalInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Congestion processor impl
 * Created by tejas0908 on 28/04/16.
 */
@Component
public class CongestionProcessorServiceImpl {

    final static Logger LOGGER = LoggerFactory.getLogger(CongestionProcessorServiceImpl.class);
    private static List<Integer> ROAD_IDS= Arrays.asList(1,2,3,4);
    private static Integer TOTAL_INTERVAL=60;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 5000)
    public void processCongestion(){
        LOGGER.info("Processing Congestion");

        //calculate congestion counts
        List<SignalInterval> intervals=new ArrayList<SignalInterval>();
        Double totalCongestionCount=0.0;
        for(Integer roadId:ROAD_IDS){
            List<RoadPing> roadpings=template.opsForList().range(roadId,0,-1);
            SignalInterval interval=new SignalInterval();
            interval.setRoadId(roadId);
            interval.setCongestionCount(roadpings.size());
            totalCongestionCount+=roadpings.size();
            intervals.add(interval);
            template.delete(roadId);
        }

        //calculate signal intervals
        for(SignalInterval interval:intervals){
            Double congestionPercentage=(interval.getCongestionCount()/totalCongestionCount)*100;
            interval.setInterval(Math.round((congestionPercentage/100)*TOTAL_INTERVAL));
        }

        sendSignalInfo(new SignalInfo(intervals));
    }

    private void sendSignalInfo(SignalInfo info){
        LOGGER.info("Calculations "+info);
        simpMessagingTemplate.convertAndSend("/topic/traffic-intervals",info);
    }
}
