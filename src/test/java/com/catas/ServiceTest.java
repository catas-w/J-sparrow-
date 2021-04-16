package com.catas;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.UserInfo;
import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostService;
import com.catas.audit.service.IHostgroupService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.HostVo;
import com.catas.audit.vo.RelatedHostVo;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.service.IJobService;
import com.catas.glimmer.service.IMultiTaskService;
import com.catas.glimmer.service.IPlanService;
import com.catas.glimmer.service.IScheduleLogService;
import com.catas.glimmer.util.SSHUtil;
import com.catas.glimmer.vo.PlanVo;
import com.catas.glimmer.vo.ScheduleLogVo;
import com.catas.glimmer.vo.TaskLogVo;
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

    @Autowired
    private IJobService jobService;

    @Autowired
    private IPlanService planService;

    @Autowired
    private IScheduleLogService scheduleLogService;

    @Autowired
    private IMultiTaskService multiTaskService;

    @Test
    void test1() {
        // List<Job> relatedJobs = planMapper.getRelatedJobs(plan.getId());
        Plan plan = planService.getById(1);
        List<Map<String, Object>> maps = planService.getRelatedBindHosts(plan);
        System.out.println(maps);
    }

    @Test
    void test2() {
        TaskLogVo taskLogVo = new TaskLogVo();
        taskLogVo.setDelay(5);
        taskLogVo.setBindHostIds(Arrays.asList(1,2));
        taskLogVo.setDescription("test");
        taskLogVo.setCmd("df -h");
        taskLogVo.setTaskType(0);
        taskLogVo.setUserId(1);
        taskLogVo.setTaskCount(taskLogVo.getBindHostIds().size());
        multiTaskService.createTask(taskLogVo);

    }


}
