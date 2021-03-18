package com.catas;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.RelatedHostVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        // relatedHostVo.setHostName("local");
        relatedHostVo.setIdc("图书馆");
        System.out.println(bindhostService.queryRelatedHosts(new Page<RelatedHostDto>(1,10),relatedHostVo));
    }

    @Test
    void test3() {
        Set<Integer> set = bindhostService.queryAllRelatedHostIds(1);
        System.out.println(set);
    }

    @Test
    void test4() {
        Map<String, String> info = bindhostService.getHostLoginInfo(1);
        System.out.println(info);
    }
}
