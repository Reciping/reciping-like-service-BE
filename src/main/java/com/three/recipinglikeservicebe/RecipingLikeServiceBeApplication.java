package com.three.recipinglikeservicebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RecipingLikeServiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipingLikeServiceBeApplication.class, args);
    }

}
