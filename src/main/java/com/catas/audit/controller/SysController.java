package com.catas.audit.controller;


import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.WebUtils;
import com.catas.audit.entity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class SysController {

    @RequestMapping("")
    public String index(Model model) {
        ActiveUser user = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user.getUserInfo().getName());
        return "audit/index";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "audit/login";
    }

    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/toLogin";
    }

}
