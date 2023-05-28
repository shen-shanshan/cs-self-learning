package com.msb.spring.redis.redis_api_practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class TestRedis {
    @Autowired
    RedisTemplate redisTemplate; // spring 注入引用

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void testRedis() {
        // test 1
//        redisTemplate.opsForValue().set("hello","china");
//        System.out.println(redisTemplate.opsForValue().get("hello"));

        // test 2
//        stringRedisTemplate.opsForValue().set("hello01","china");
//        System.out.println(stringRedisTemplate.opsForValue().get("hello01"));

        // test 3
//        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
//        conn.set("hello02".getBytes(),"mashibing".getBytes());
//        System.out.println(new String(conn.get("hello02".getBytes())));

        // test 4
//        HashOperations<String,Object,Object> hash = stringRedisTemplate.opsForHash();
//        hash.put("sean","name","shenshanshan");
//        hash.put("sean","age","23");
//        System.out.println(hash.entries("sean"));

        // test 5
        Person p = new Person();
        p.setName("zhangsan");
        p.setAge(16);
        stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        // 以 hashmap 的形式直接存入整个对象
        redisTemplate.opsForHash().putAll("sean01", jm.toHash(p));
        // 取出 key 对应的 map 并打印其信息
        Map map = redisTemplate.opsForHash().entries("sean01");
        Person person = objectMapper.convertValue(map, Person.class);
        System.out.println(person.getName());
    }
}
