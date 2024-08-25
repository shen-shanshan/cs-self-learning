package com.msb.spring.redis.redis_api_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RedisApiPracticeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RedisApiPracticeApplication.class, args);
        TestRedis redis = ctx.getBean(TestRedis.class); // 获取容器里的类的对象（引用）
        redis.testRedis();
    }

}
