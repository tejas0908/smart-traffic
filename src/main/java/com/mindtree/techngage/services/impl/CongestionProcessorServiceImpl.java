package com.mindtree.techngage.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mindtree.techngage.entity.RoadPing;
import com.mindtree.techngage.entity.SignalInfo;
import com.mindtree.techngage.entity.SignalInterval;
import com.mindtree.techngage.services.CongestionProcessorService;

/**
 * Congestion processor impl
 * Created by tejas0908 on 28/04/16.
 */
@Component
public class CongestionProcessorServiceImpl implements CongestionProcessorService{

    final static Logger LOGGER = LoggerFactory.getLogger(CongestionProcessorServiceImpl.class);
    //private static List<Integer> ROAD_IDS= Arrays.asList(1,2,3,4);
    private static Integer TOTAL_INTERVAL=20000;

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
        List<Integer> roadIds=(List<Integer>)template.opsForList().range("roads", 0, -1).stream().distinct().collect(Collectors.toList());
        for(Integer roadId:roadIds){
            List<RoadPing> roadpings=template.opsForList().range(roadId,0,-1);
            SignalInterval interval=new SignalInterval();
            interval.setRoadId(roadId);
            interval.setCongestionCount(roadpings.size());
            totalCongestionCount+=roadpings.size();
            intervals.add(interval);
            template.delete(roadId);
        }
        template.delete("roads");

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
