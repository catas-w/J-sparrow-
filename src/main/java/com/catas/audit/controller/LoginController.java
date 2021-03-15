package com.catas.audit.controller;


import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.ResultObj;
import com.catas.audit.common.WebUtils;
import com.catas.audit.entity.UserInfo;
import com.catas.audit.service.IUserInfoService;
import com.catas.audit.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IUserInfoService userInfoService;

    @PostMapping("")
    public ResultObj login(UserVo userVo, HttpSession session) {
        // System.out.println("userVo = " + userVo);
        String code = (String) session.getAttribute("code");
        if (userVo.getCode()==null || !userVo.getCode().equals(code)) {
            // 验证码不正确
        }

        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken passwordToken = new UsernamePasswordToken(userVo.getName(), userVo.getPassword(), userVo.getRememberMe());
        try {
            subject.login(passwordToken);
            ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
            UserInfo curUser = activeUser.getUserInfo();
            WebUtils.getSession().setAttribute("user", curUser);
            curUser.setLastLogin(new Date());
            userInfoService.updateById(curUser);

            return ResultObj.LOGIN_SUCCESS;
        }catch (UnknownAccountException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_UNKNOW_USER;
        }catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }

}
