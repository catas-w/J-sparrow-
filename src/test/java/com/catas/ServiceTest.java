package com.catas;

import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.RelatedHostVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServiceTest {

    @Autowired
    IUserInfoService userInfoService;

    @Autowired
    private BindhostMapper bindhostMapper;

    @Autowired
    private IBindhostService bindhostService;

    @Test
    void test1() {
        List<Integer> list = userInfoService.queryGroupIdsByUserId(1);
        System.out.println(list);
    }

    @Test
    void test2() {
        RelatedHostVo relatedHostVo = new RelatedHostVo();
        relatedHostVo.setUserId(1);
        relatedHostVo.setHostName("local");
        relatedHostVo.setIdcId(1);
        // System.out.println(bindhostService.queryRelatedHosts(relatedHostVo));
    }
}
