package com.catas;

import com.catas.audit.service.IUserInfoService;
import com.catas.audit.config.ShiroAutoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SparrowApplicationTests {


    @Autowired
    ShiroAutoConfig shiroAutoConfig;

    @Autowired
    IUserInfoService userInfoService;

    @Test
    void contextLoads() {
    }

    @Test
    void testShiroArgs() {
        System.out.println("shiroAutoConfig.getLoginUrl() = " + shiroAutoConfig.getLoginUrl());
    }

    @Test
    void testService() {
        System.out.println(userInfoService.list());
        // List<Integer>[] graph = new List<>[1];
        ArrayList<Integer> list = new ArrayList<>();
    }
}
