package com.mindtree.techngage.controllers;

import com.mindtree.techngage.entity.RoadPing;
import com.mindtree.techngage.entity.SignalInfo;
import com.mindtree.techngage.services.RegisterPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Main controller
 * Created by tejas0908 on 24/04/16.
 */
@Controller
public class MainController {

    final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private RegisterPingService registerPingService;

    /**
     * Method receives a road ping from any road.
     * it proceeds to insert this in a redis db
     * @param ping
     * @throws Exception
     */
    @MessageMapping("/road-ping")
    public void registerPing(RoadPing ping) throws Exception {
        LOGGER.info("Received Road Ping "+ping);
        registerPingService.registerPing(ping);
    }

    /**
     * handles request to the root url and serves index.html
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        return "index";
    }

    @RequestMapping("/test")
    public String test(Map<String, Object> model) {
        return "test";
    }
}
