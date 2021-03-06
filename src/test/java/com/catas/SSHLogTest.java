package com.catas;

import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.service.IPlanService;
import com.catas.webssh.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class SSHLogTest {

    @Autowired
    private LogUtil logUtil;

    @Autowired
    private IPlanService planService;

    @Test
    void test1() {
        Plan plan = planService.getById(2);
        File log = logUtil.initScheduleLog(plan);
        // logUtil.log('test', log);
        // logUtil.log('What's happening ', log);
        System.out.println(log);

    }

    @Test
    void testWriteToFile() {
        Plan plan = planService.getById(8);
        File logFile = logUtil.initScheduleLog(plan);

        // System.out.println(logFile.toString());

        logUtil.log("Boston", logFile);
        logUtil.log("Los Angeles", logFile);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logUtil.log("Django unchained", logFile);


    }
}
