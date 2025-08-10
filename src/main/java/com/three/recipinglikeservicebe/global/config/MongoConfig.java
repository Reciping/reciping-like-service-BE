package com.three.recipinglikeservicebe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
// 안전하게 레포지토리 패키지 지정 (자동으로도 잡히지만 명시해두면 확실)
@EnableMongoRepositories(basePackages = "com.three.recipinglikeservicebe.like.repository")
public class MongoConfig {

    // 이름이 "mongoTemplate"인 빈을 명시적으로 등록 (Spring Data가 이 이름을 기본으로 찾음)
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoConverter converter) {
        return new MongoTemplate(mongoDbFactory, converter);
    }
}
