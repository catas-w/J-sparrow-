package com.catas.audit.controller.admin;


import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.entity.Host;
import com.catas.audit.entity.Hostuser;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostService;
import com.catas.audit.service.IHostuserService;
import com.catas.audit.vo.BindHostVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bindhost")
public class BindHostController {

    @Autowired
    private IBindhostService bindhostService;

    @Autowired
    private IHostService hostService;

    @Autowired
    private IHostuserService hostuserService;

    @RequestMapping("/get-all")
    public DataGridView getAllBindHostInfo(BindHostVo bindHostVo) {
        List<Map<String, Object>> allBindHost = bindhostService.getAllBindHost(bindHostVo);
        return new DataGridView((long) allBindHost.size(), allBindHost);
    }

    // 主机选择框
    @RequestMapping("/get-host-list")
    public DataGridView getHostUserList() {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Host> list = hostService.list();
        for (Host host : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", host.getId());
            map.put("name", host.getHostName() + "[" + host.getIpAddress() + "]" );
            res.add(map);
        }
        return new DataGridView((long) res.size(), res);
    }

    // 用户选择框
    @RequestMapping("/get-user-list")
    public DataGridView getHostList() {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Hostuser> hostusers = hostuserService.list();
        for (Hostuser hostuser : hostusers) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", hostuser.getId());
            map.put("name", hostuser.getUsername());
            res.add(map);
        }
        return new DataGridView((long) res.size(), res);
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/add")
    public ResultObj addBindHost(BindHostVo bindHostVo) {
        try {
            bindhostService.save(bindHostVo);
            return ResultObj.ADD_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "绑定信息已存在");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/update")
    public ResultObj updateBindHost(BindHostVo bindHostVo) {
        try {
            bindhostService.updateById(bindHostVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "绑定信息已存在");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/delete/{id}")
    public ResultObj updateBindHost(@PathVariable("id") Integer bindHostId) {
        try {
            bindhostService.removeById(bindHostId);
            return ResultObj.DELETE_SUCCESS;
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }
}
