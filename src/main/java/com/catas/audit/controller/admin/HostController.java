package com.catas.audit.controller.admin;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Host;
import com.catas.audit.entity.Hostgroup;
import com.catas.audit.entity.Idc;
import com.catas.audit.service.*;
import com.catas.audit.vo.HostVo;
import com.catas.audit.vo.RelatedHostVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("/host")
@RestController
public class HostController {

    @Autowired
    private IHostgroupService hostgroupService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IBindhostService bindhostService;

    @Autowired
    private IHostService hostService;

    @Autowired
    private IIdcService idcService;

    @RequestMapping("/get-host-group")
    public DataGridView getHostGroup() {
        // 用户绑定的主机组
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        List<Integer> groupIds = userInfoService.queryGroupIdsByUserId(activeUser.getUserInfo().getId());
        Collection<Hostgroup> hostgroups = hostgroupService.listByIds(groupIds);
        return new DataGridView(hostgroups);
    }

    // 当前用户的主机信息
    @RequestMapping("/hostList")
    public DataGridView getHostList(RelatedHostVo relatedHostVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        relatedHostVo.setUserId(activeUser.getUserInfo().getId());
        Page<RelatedHostDto> page = new Page<>(relatedHostVo.getPage(), relatedHostVo.getLimit());

        if (relatedHostVo.getGroupId() == null){
            // TODO: 改为全部主机
            bindhostService.queryRelatedHosts(page, relatedHostVo);
        }else {
            bindhostService.queryBindHostsByUserGroup(page, relatedHostVo);
        }

        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     *  -------admin--------
     * @return json
     */
    // 获取所有主机信息
    @RequestMapping("/get-all-host-info")
    public DataGridView getAllHostInfo(HostVo hostVo) {

        List<Map<String, Object>> allHostInfo = hostService.getAllHostInfo(hostVo);
        return new DataGridView((long) allHostInfo.size(), allHostInfo);
    }

    // 获取idc列表
    @RequestMapping("/get-idc-list")
    public DataGridView getIdcList() {
        List<Idc> idcs = idcService.list();
        return new DataGridView((long) idcs.size(), idcs);
    }

    @RequestMapping("/add")
    public ResultObj addHost(HostVo hostVo) {
        try {
            if (hostVo.getIdcId() == -1) {
                hostVo.setIdcId(null);
            }
            hostService.save(hostVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequestMapping("/update")
    public ResultObj updateHost(HostVo hostVo) {
        try {
            if (hostVo.getIdcId() == -1) {
                hostVo.setIdcId(null);
                hostService.update(null,
                        Wrappers.<Host>lambdaUpdate()
                        .set(Host::getHostName, hostVo.getHostName())
                        .set(Host::getIdcId, null)
                        .set(Host::getEnabled, hostVo.getEnabled())
                        .set(Host::getIpAddress, hostVo.getIpAddress())
                        .set(Host::getPort, hostVo.getPort())
                        .eq(Host::getId, hostVo.getId())
                );
            }else
                hostService.updateById(hostVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultObj deleteHost(@PathVariable("id") Integer hostId) {
        try {
            hostService.removeById(hostId);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }
}
