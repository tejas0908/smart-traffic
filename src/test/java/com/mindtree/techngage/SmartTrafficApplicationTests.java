package com.mindtree.techngage;

import com.mindtree.techngage.controllers.MainController;
import com.mindtree.techngage.entity.RoadPing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = SmartTrafficApplication.class)
//@WebAppConfiguration
public class SmartTrafficApplicationTests {

    @Autowired
    private MainController controller;

	//@Test
	public void test() throws Exception{
        while(true){
            Long value=Math.round(Math.random()*100);
            Integer roadId=null;
            if(value<50)
                roadId=3;
            else if(value<65)
                roadId=1;
            else if(value<75)
                roadId=2;
            else
                roadId=4;

            controller.registerPing(new RoadPing(roadId));
            Thread.sleep(500);
        }
	}
}
