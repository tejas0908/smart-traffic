package com.mindtree.techngage.services.impl;

import com.mindtree.techngage.entity.RoadPing;
import com.mindtree.techngage.services.RegisterPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Register Ping Service Impl
 * Created by tejas0908 on 28/04/16.
 */
@Component
public class RegisterPingServiceImpl implements RegisterPingService{

    final static Logger LOGGER = LoggerFactory.getLogger(RegisterPingServiceImpl.class);

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Override
    public void registerPing(RoadPing ping) throws Exception {
        LOGGER.info("Inserting ping into redis "+ping);
        template.opsForList().leftPush("roads", ping.getRoadId());
        template.opsForList().leftPush(ping.getRoadId(),ping);
    }
}
