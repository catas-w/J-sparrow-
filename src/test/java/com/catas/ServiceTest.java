package com.catas;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostService;
import com.catas.audit.service.IHostgroupService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.HostVo;
import com.catas.audit.vo.RelatedHostVo;
import com.catas.webssh.config.WebSSHWebSocketConfig;
import com.catas.webssh.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
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

    @Autowired
    WebSSHWebSocketConfig webSocketConfig;

    @Autowired
    IHostgroupService hostgroupService;

    @Autowired
    IHostService hostService;

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
    void test6() {
        HostVo hostVo = new HostVo();
        hostVo.setHostName("loca");
        System.out.println(hostService.getAllHostInfo(hostVo));
    }

    @Test
    void test3() {
        // Set<Integer> set = bindhostService.queryAllRelatedHostIds(1);
        // System.out.println(set);

        // System.out.println(hostgroupService.getAllGroupInfo());
        System.out.println("***************************");
        System.out.println(hostgroupService.queryRelatedGroupIds(1));
        hostgroupService.updateRelatedGroups(1, Arrays.asList(2,3,4));
        System.out.println("***************************");
        System.out.println(hostgroupService.queryRelatedGroupIds(1));

    }

    @Test
    void test4() {
        System.out.println(bindhostService.queryAllRelatedHostIds(1));
        System.out.println("----------------------");
        // bindhostMapper.deleteRelatedHostByUserId(1);
        // bindhostMapper.saveRelatedHost(1, Arrays.asList(1,2,4));
        bindhostService.updateUserBindHosts(1, Arrays.asList(5,6,7));
        System.out.println(bindhostService.queryAllRelatedHostIds(1));
    }

    @Test
    void test5() {
        RelatedHostVo hostVo = new RelatedHostVo();
        hostVo.setUserId(1);
        // hostVo.setGroupId(1);
        System.out.println(bindhostService.queryBindHostsByUserGroup(new Page<RelatedHostDto>(1,10), hostVo));
    }

}
