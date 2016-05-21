package com.mindtree.techngage.services.impl;

import com.mindtree.techngage.entity.BoundingBox;
import com.mindtree.techngage.entity.MLItem;
import com.mindtree.techngage.entity.MLResponse;
import com.mindtree.techngage.entity.SignalTime;
import com.mindtree.techngage.services.ImageDetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of image detection service.
 * Created by tejas0908 on 20/05/16.
 */
@Component
public class ImageDetectionServiceImpl implements ImageDetectionService {

    final static Logger LOGGER = LoggerFactory.getLogger(ImageDetectionServiceImpl.class);

    @Value("${ml.detect.url}")
    private String URL;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SignalTime detectImages(Integer roadId, final MultipartFile file) throws Exception {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getName();
            }
        });
        MLResponse response = restTemplate.postForObject(URL, parts, MLResponse.class);

        //filter in cars
        List<MLItem> cars = response.getResult().stream().filter(item -> {
            if (item.getCategory().equalsIgnoreCase("car")) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        SignalTime signalTime = new SignalTime();
        signalTime.setRoadId(roadId);
        signalTime.setTime(0);
        signalTime.setVehicleCount(cars.size());

        List<BoundingBox> boxes = cars.stream().map(car -> {
            List<Double> points = car.getBbox();
            BoundingBox box = new BoundingBox(points.get(0), points.get(1), points.get(2), points.get(3));
            return box;
        }).collect(Collectors.toList());
        signalTime.setBoxes(boxes);

        return signalTime;
    }
}
