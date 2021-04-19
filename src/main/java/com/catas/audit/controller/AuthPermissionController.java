package com.catas.audit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.entity.AuthGroup;
import com.catas.audit.entity.AuthGroupPermissions;
import com.catas.audit.entity.AuthPermission;
import com.catas.audit.service.IAuthGroupPermissionsService;
import com.catas.audit.service.IAuthGroupService;
import com.catas.audit.service.IAuthPermissionService;
import com.catas.audit.vo.PermissionVo;
import com.catas.audit.vo.UserGroupVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author catas
 * @since 2021-04-18
 */
@RestController
@RequestMapping("/admin/permission")
@Validated
public class AuthPermissionController {

    @Autowired
    private IAuthPermissionService permissionService;

    @Autowired
    private IAuthGroupService groupService;

    @Autowired
    private IAuthGroupPermissionsService groupPermissionsService;

    @RequestMapping("/list")
    public DataGridView getPermissionList(PermissionVo permissionVo) {
        Page<AuthPermission> permissionPage = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        permissionService.page(permissionPage);
        return new DataGridView(permissionPage.getTotal(), permissionPage.getRecords());
    }

    @RequestMapping("/add")
    public ResultObj addPermission(PermissionVo permissionVo) {
        try {
            permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequestMapping("/update")
    public ResultObj updatePermission(PermissionVo permissionVo) {
        try {
            permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultObj deletePermission(@PathVariable("id") Integer permId) {
        try {
            permissionService.removeById(permId);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }


    // ---- groups
    @RequestMapping("/group/list")
    public DataGridView getAllGroups() {
        QueryWrapper<AuthGroup> queryWrapper = new QueryWrapper<>();
        List<AuthGroup> groupList = groupService.list();
        return new DataGridView(groupList);
    }

    /**
     *
     * @Description: 获取当前组关联的权限id
     */
    @RequestMapping("/group/get-perms/{id}")
    public DataGridView getGroupRelatedPerms(@PathVariable("id") Integer groupId) {
        QueryWrapper<AuthGroupPermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        queryWrapper.select("permission_id");
        List<AuthGroupPermissions> permissions = groupPermissionsService.list(queryWrapper);

        Map<String, List<Integer>> res = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (AuthGroupPermissions obj : permissions) {
            list.add(obj.getPermissionId());
        }
        res.put("relatedPermList", list);
        return new DataGridView(res);
    }

    @RequiresPermissions("permission:edit")
    @RequestMapping("/group/add")
    public ResultObj addGroup(@Valid UserGroupVo userGroupVo) {

        try {
            groupService.save(userGroupVo);
            Integer id = userGroupVo.getId();
            groupService.updateGroupPerms(id, userGroupVo.getRelatedPermList());
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequiresPermissions("permission:edit")
    @RequestMapping("/group/update")
    public ResultObj updateGroup(@Validated UserGroupVo userGroupVo) {

        try {
            groupService.updateById(userGroupVo);
            Integer id = userGroupVo.getId();
            groupService.updateGroupPerms(id, userGroupVo.getRelatedPermList());
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("permission:edit")
    @RequestMapping("/group/delete/{id}")
    public ResultObj releteGroup(@PathVariable("id") @NotNull(message = "id?") Integer groupId) {

        try {
            groupService.removeById(groupId);
            groupService.updateGroupPerms(groupId, null);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }
}

