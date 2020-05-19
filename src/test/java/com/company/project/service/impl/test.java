package com.company.project.service.impl;

import com.company.project.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
public class test {
    @Value("${spring.redis.port}")
    private static int PORT;
    private static String ADDR;
    @Value("${spring.redis.password}")
    private static String AUTH;

    @Value("${spring.redis.port}")
    public  void setPort(int port) {
        test.PORT = port;
    }
    @Value("${spring.redis.addr}")
    public  void setAddr(String addr) {
        test.ADDR = addr;
    }
    @Value("${spring.redis.password}")
    public  void setAuth(String password) {
        test.AUTH = password;
    }

    @Test
    public void testFindByNamePhone() {
//        System.out.println(templateId);
//        System.out.println(PORT);
//        System.out.println(AUTH);
//        System.out.println(ADDR);
//        System.out.println(StringUtils.isEmpty(null));
//        System.out.println(StringUtils.isEmpty(""));
//        System.out.println(StringUtils.isEmpty(" "));  //注意在StringUtils中空格作非空处理
//
//        System.out.println(StringUtils.isNotBlank(null));
//        System.out.println(StringUtils.isBlank(""));
//        System.out.println(StringUtils.isBlank(" "));
//        System.out.println(StringUtils.isBlank("\t \n \f \r"));
        Set<String> delayBucket2 = RedisUtil.getZsetByMaxMinRegion("delayBucket", 1589536800020L, 0L, 2);
        System.out.println(delayBucket2);
    }

    @Test
    public void finishbyvelue() {

        Long delayBucket2 = RedisUtil.delZset("delayBucket", 2, "{\"@class\":\"com.yangwenjie.delayqueue.core.ScoredSortedItem\",\"delayQueueJodId\":710903123153129472,\"delayTime\":1589536800001}");

        System.out.println(delayBucket2);
    }
    @Test
    public void finishByScore() {

        Long delayBucket2 = RedisUtil.delZset("delayBucket", 2, "{\"@class\":\"com.yangwenjie.delayqueue.core.ScoredSortedItem\",\"delayQueueJodId\":710903123153129472,\"delayTime\":1589536800001}");

        System.out.println(delayBucket2);
    }
    @Test
    public void deleteScore() {

        Long delayBucket2 = RedisUtil.delZsetByScore("delayBucket", 0, 1589536800020L,2);

        System.out.println(delayBucket2);
    }
    @Test
    public void zsetAdd() {

        Long delayBucket2 = RedisUtil.zsetAdd("delayBucket", 1589536800020L, "test",2);
        Long delayBucket3 = RedisUtil.zsetAdd("delayBucket", 1589536800060L, "test1",2);

        System.out.println(delayBucket2);
    }
    //同意访问时插入时间
    @Test
    public void agree() {

        Long delayBucket2 = RedisUtil.zsetAdd("delayBucket2", 1589536800020L, "test",5);
        Long delayBucket3 = RedisUtil.zsetAdd("delayBucket2", 1589536800060L, "test1",5);

        System.out.println(delayBucket2);
    }
    }

