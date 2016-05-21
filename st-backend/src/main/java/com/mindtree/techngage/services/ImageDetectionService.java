package com.mindtree.techngage.services;

import com.mindtree.techngage.entity.SignalTime;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface defines the methods to be implemented by image detection methods.
 * Created by tejas0908 on 20/05/16.
 */
public interface ImageDetectionService {
    SignalTime detectImages(Integer roadId, MultipartFile file) throws Exception;
}
