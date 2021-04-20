package com.catas.audit.controller.admin;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.dto.UserInfoDto;
import com.catas.audit.entity.UserInfo;
import com.catas.audit.service.IAuthGroupService;
import com.catas.audit.service.IBindhostService;
import com.catas.audit.service.IHostgroupService;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.PasswordVo;
import com.catas.audit.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author catas
 * @since 2021-03-14
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IBindhostService bindhostService;

    @Autowired
    private IHostgroupService hostgroupService;

    @Autowired
    private IAuthGroupService authGroupService;

    @RequestMapping("/get-user-list")
    public DataGridView loadUserInfo(UserVo userVo) {

        IPage<UserInfo> page = new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(userVo.getName()), "name", userVo.getName());
        queryWrapper.like(StringUtils.isNoneBlank(userVo.getEmail()), "email", userVo.getEmail());
        queryWrapper.eq(userVo.getIsActive()!=null, "is_active", 1);

        userInfoService.page(page, queryWrapper);
        page.convert(userInfo -> {
            UserInfoDto dto = new UserInfoDto();
            BeanUtils.copyProperties(userInfo, dto);
            return dto;
        } );

        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @RequiresPermissions("user:edit")
    @RequestMapping("/update")
    public ResultObj updateUser(UserVo userVo) {

        try {
            userInfoService.updateById(userVo);
            authGroupService.updateUserRelatedGroup(userVo.getId(), userVo.getRelatedGroupList());
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("user:edit")
    @RequestMapping("/add")
    public ResultObj addUser(UserVo userVo) {
        String exist = userInfoExist(userVo);
        if (!exist.equals("")) {
            return new ResultObj(0, exist);
        }

        try {
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);
            String pwd = new Md5Hash(Constant.DEFAULT_PWS, salt, 2).toString();
            userVo.setPassword(pwd);
            userInfoService.save(userVo);
            // 添加权限组
            if (userVo.getRelatedGroupList()!= null && !userVo.getRelatedGroupList().isEmpty())
                authGroupService.updateUserRelatedGroup(userVo.getId(), userVo.getRelatedGroupList());

            return ResultObj.ADD_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(0, "用户名或邮箱已存在");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    // 判断用户名或邮箱是否已存在
    public String userInfoExist(UserVo userVo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userVo.getEmail());
        if (userInfoService.list(queryWrapper).size() > 0)
            return "Email already exists!";
        queryWrapper.or().eq("name", userVo.getName());
        if (userInfoService.list(queryWrapper).size() > 0)
            return "User name already exists!";
        return "";
    }

    @RequiresPermissions("user:edit")
    @RequestMapping("/delete/{id}")
    public ResultObj deleteUser(@PathVariable("id") Integer userId) {

        try {
            userInfoService.removeById(userId);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }

    // 初始化 bindhosts 列表
    @RequestMapping("/get-bind-host-list/{id}")
    public DataGridView getBindHostList(@PathVariable("id") Integer userId) {
        List<Map<String, Object>> allHostInfo = bindhostService.getAllHostInfo();
        Set<Integer> hostIds = bindhostService.queryAllRelatedHostIds(userId);
        for (Map<String, Object> map : allHostInfo) {
            boolean LAY_CHECKED = false;
            Integer bindHostId = (Integer) map.get("id");
            if (hostIds.contains(bindHostId)) {
                LAY_CHECKED = true;
            }
            map.put("LAY_CHECKED", LAY_CHECKED);
        }
        return new DataGridView((long) allHostInfo.size(), allHostInfo);
    }

    // 更新用户绑定主机信息
    @RequiresPermissions("user:edit")
    @RequestMapping("/save-bind-hosts")
    public ResultObj saveBindHosts(UserVo userVo) {

        try {
            bindhostService.updateUserBindHosts(userVo.getId(), userVo.getBindHostIds());
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    // 初始化主机组列表
    @RequestMapping("/get-host-group-list/{id}")
    public DataGridView getHostGroupList(@PathVariable("id") Integer userId) {
        List<Map<String, Object>> allGroupInfo = hostgroupService.getAllGroupInfo();
        Set<Integer> relatedGroupIds = hostgroupService.queryRelatedGroupIds(userId);
        for (Map<String, Object> map : allGroupInfo) {
            boolean LAY_CHECKED = false;
            Integer id = (Integer) map.get("id");
            if (relatedGroupIds.contains(id)) {
                LAY_CHECKED = true;
            }
            map.put("LAY_CHECKED", LAY_CHECKED);
        }
        return new DataGridView((long) allGroupInfo.size(), allGroupInfo);
    }

    // 保存主机组
    @RequiresPermissions("user:edit")
    @RequestMapping("/save-host-group")
    public ResultObj saveBindGroups(UserVo userVo) {

        try {
            hostgroupService.updateRelatedGroups(userVo.getId(), userVo.getBindGroupIds());
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    /**
     * @Description: 修改密码
     */
    @RequestMapping("/modify-password")
    public ResultObj modifyPassword(@Validated PasswordVo passwordVo) {

        ActiveUser activateUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        UserInfo curUser = activateUser.getUserInfo();
        String oldPassword = passwordVo.getOldPassword();
        String oldPwdHash = new Md5Hash(oldPassword, curUser.getSalt(), 2).toString();
        if (!curUser.getPassword().equals(oldPwdHash)) {
            return ResultObj.error("密码不正确");
        }
        if (!passwordVo.getNewPassword().equals(passwordVo.getAgainPassword()))
            return ResultObj.error("两次输入密码不一致");

        try {
            String salt = IdUtil.simpleUUID().toUpperCase();
            String pwd = new Md5Hash(Constant.DEFAULT_PWS, curUser.getSalt(), 2).toString();
            curUser.setPassword(pwd);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }
}

