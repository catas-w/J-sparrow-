package com.catas.audit.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.Constant;
import com.catas.audit.entity.UserInfo;
import com.catas.audit.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    IUserInfoService userInfoService;

    /**
     * @Description: 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权....");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        ActiveUser activeUser = (ActiveUser) principalCollection.getPrimaryPrincipal();
        UserInfo userInfo = activeUser.getUserInfo();
        if (userInfo.getIsAdmin().equals(Constant.USER_IS_ADMIN)) {
            info.addStringPermissions(Arrays.asList("*:*"));
        }else {
            List<String> permissions = userInfoService.getPermissionsById(userInfo.getId());
            info.addStringPermissions(permissions);
        }
        log.info("授权结束...");
        return info;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // token.getPrincipal();
        queryWrapper.eq("name", token.getUsername());
        UserInfo userInfo = userInfoService.getOne(queryWrapper);
        if (userInfo == null) {
            return null;
        }
        if (userInfo.getIsActive() == 0) {
            throw new LockedAccountException();
        }

        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserInfo(userInfo);
        // 为 activeUser 添加权限
        ByteSource salt = ByteSource.Util.bytes(userInfo.getSalt());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser, userInfo.getPassword(), salt, this.getName());

        return info;
    }
}
