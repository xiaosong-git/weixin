package com.company.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.company.project.model.Company;
import com.company.project.service.CompanyService;
import com.company.project.service.UserService;
import com.company.project.util.RedisUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
public class UserServiceImplTest {
    @Resource
    private UserService userService;
    @Resource
    private CompanyService companyService;
    @Ignore
    @Test
    public void testFindByNamePhone() {
        List<Company> companyList = null;
        String key = "小松";
        //redis修改
        String json = RedisUtil.getStrVal(key, 4);
        if(StrUtil.isNotBlank(json)&&!"null".equals(json)){
            System.out.println("null".equals(json));
            //先从缓存中获取
            companyList = JSON.parseObject(json, List.class);
        }else {
            companyList = companyService.findCompany("小");
            json = JSON.toJSONString(companyList);
            System.out.println(json);
            RedisUtil.setStr(key, json, 4, 24*3600);
        }
    }


}