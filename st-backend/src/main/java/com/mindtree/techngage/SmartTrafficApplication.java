package com.mindtree.techngage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import redis.embedded.RedisServer;

@SpringBootApplication
public class SmartTrafficApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTrafficApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

    @Bean
    public RedisServer redis() throws Exception {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();
        return redisServer;
    }
}
