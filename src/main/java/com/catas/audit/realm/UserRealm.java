package com.catas.audit.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.entity.UserInfo;
import com.catas.audit.service.IUserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    IUserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
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
