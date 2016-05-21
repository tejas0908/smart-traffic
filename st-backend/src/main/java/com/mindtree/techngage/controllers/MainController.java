package com.mindtree.techngage.controllers;

import com.mindtree.techngage.entity.RoadPing;
import com.mindtree.techngage.entity.SignalTime;
import com.mindtree.techngage.entity.SignalTimes;
import com.mindtree.techngage.services.RegisterPingService;
import com.mindtree.techngage.services.ImageDetectionService;
import com.mindtree.techngage.services.SignalTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main controller where all the rest endpoints are defined.
 * This class hosts the get signal times method.
 * Created by tejas0908 on 24/04/16.
 */
@Controller
public class MainController {

    final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private RegisterPingService registerPingService;

    @Autowired
    private ImageDetectionService imageDetectionService;

    @Autowired
    private SignalTimeService signalTimeService;

    /**
     * Method receives a road ping from any road.
     * it proceeds to insert this in a redis db
     *
     * @param ping
     * @throws Exception
     */
    @MessageMapping("/road-ping")
    public void registerPing(RoadPing ping) throws Exception {
        LOGGER.info("Received Road Ping " + ping);
        registerPingService.registerPing(ping);
    }

    /**
     * handles request to the root url and serves index.html
     *
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

    @RequestMapping("/video-test")
    public String videoTest(Map<String, Object> model) {
        return "video-test";
    }


    /**
     * This method takes 4 images of a juntion as input and responds with the vehicle count and signal times for each one.
     *
     * @param road2
     * @param road4
     * @param road6
     * @param road8
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/get-signal-times", method = RequestMethod.POST)
    @ResponseBody
    public SignalTimes getSignalTimes(@RequestParam("road2") MultipartFile road2,
                                      @RequestParam("road4") MultipartFile road4,
                                      @RequestParam("road6") MultipartFile road6,
                                      @RequestParam("road8") MultipartFile road8
    ) throws Exception {
        //detecting instances of cars in the uploaded images
        SignalTimes signalTimes = new SignalTimes();
        List<SignalTime> signalTimeList = new ArrayList<>();
        signalTimeList.add(imageDetectionService.detectImages(2, road2));
        signalTimeList.add(imageDetectionService.detectImages(4, road4));
        signalTimeList.add(imageDetectionService.detectImages(6, road6));
        signalTimeList.add(imageDetectionService.detectImages(8, road8));
        signalTimes.setSignalTimes(signalTimeList);

        //calculating signal times based on vehicleCount
        signalTimes = signalTimeService.calculateSignalTimes(signalTimes);
        return signalTimes;
    }
}
