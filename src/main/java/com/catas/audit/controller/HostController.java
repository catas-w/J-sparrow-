package com.catas.audit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.DataGridView;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Hostgroup;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostgroupService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.RelatedHostVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RequestMapping("/host")
@RestController
public class HostController {

    @Autowired
    private IHostgroupService hostgroupService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IBindhostService bindhostService;

    @RequestMapping("/get-host-group")
    public DataGridView getHostGroup() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        List<Integer> groupIds = userInfoService.queryGroupIdsByUserId(activeUser.getUserInfo().getId());
        Collection<Hostgroup> hostgroups = hostgroupService.listByIds(groupIds);
        return new DataGridView(hostgroups);
    }

    @RequestMapping("/hostList")
    public DataGridView getHostList(RelatedHostVo relatedHostVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        relatedHostVo.setUserId(activeUser.getUserInfo().getId());
        Page<RelatedHostDto> page = new Page<>(relatedHostVo.getPage(), relatedHostVo.getLimit());

        bindhostService.queryRelatedHosts(page, relatedHostVo);

        return new DataGridView(page.getTotal(), page.getRecords());
    }
}
