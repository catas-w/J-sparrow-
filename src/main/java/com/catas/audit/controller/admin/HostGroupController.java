package com.catas.audit.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.entity.Hostgroup;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostgroupService;
import com.catas.audit.vo.HostGroupVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/hostgroup")
public class HostGroupController {

    @Autowired
    private IHostgroupService hostgroupService;

    @Autowired
    private IBindhostService bindhostService;

    // 所有主机组列表
    @RequestMapping("/get-all")
    public DataGridView getAllGroup(HostGroupVo hostGroupVo) {

        List<Map<String, Object>> allGroupInfo = hostgroupService.getAllGroupInfo(hostGroupVo);
        return new DataGridView((long) allGroupInfo.size(), allGroupInfo);
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/add")
    public ResultObj addGroup(HostGroupVo hostGroupVo) {
        if (hostGroupVo.getName() == null || hostGroupVo.getName().equals("")) {
            return new ResultObj(Constant.ERROR, "名称不能为空");
        }
        try {
            hostgroupService.save(hostGroupVo);
            return ResultObj.ADD_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "名称已存在");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/update")
    public ResultObj updateGroup(HostGroupVo hostGroupVo) {
        if (hostGroupVo.getName() == null || hostGroupVo.getName().equals("")) {
            return new ResultObj(Constant.ERROR, "名称不能为空");
        }
        try {
            hostgroupService.updateById(hostGroupVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "名称已存在");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/delete/{id}")
    public ResultObj deleteGroup(@PathVariable("id") Integer groupId) {
        try {
            hostgroupService.removeById(groupId);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }


    // 获取所有 bindhost列表
    @RequestMapping("/get-bind-host-list/{id}")
    public DataGridView getBindHostList(@PathVariable("id") Integer groupId) {
        // 所有 bind-host
        List<Map<String, Object>> allHostInfo = bindhostService.getAllHostInfo();
        // 当前组关联的所有 bind-host id
        Set<Integer> relatedBindHostIds = hostgroupService.getAllRelatedBindHostIds(groupId);

        for (Map<String, Object> map : allHostInfo) {
            boolean LAY_CHECKED = false;
            Integer id = (Integer) map.get("id");
            if (relatedBindHostIds.contains(id)) {
                LAY_CHECKED = true;
            }
            map.put("LAY_CHECKED", LAY_CHECKED);
        }

        return new DataGridView((long) allHostInfo.size(), allHostInfo);
    }

    @RequestMapping("/save-bind-hosts")
    public ResultObj saveBindHosts(HostGroupVo hostGroupVo) {

        if (hostGroupVo.getId() == null) {
            return new ResultObj(Constant.ERROR, "请求格式不正确");
        }
        try {
            hostgroupService.updateRelatedBindHosts(hostGroupVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }
}
