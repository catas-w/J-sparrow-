package com.catas.audit.controller;


import com.catas.audit.common.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SysController {

    @RequestMapping("")
    public String index(Model model) {
        ActiveUser user = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user.getUserInfo().getName());
        return "audit/index";
    }

    @RequestMapping("/home")
    public String home() {
        return "audit/home";
    }

    // 登录页面
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "audit/login";
    }

    // 登出
    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/toLogin";
    }

    // 主机列表
    @RequestMapping("/host")
    public String myHosts() {
        return "audit/host_list";
    }

    // SSH登录页面
    @RequestMapping("/connect/{id}")
    public String connect(@PathVariable("id") Integer bindHostId, Model model) {
        model.addAttribute("bindHostId", bindHostId);
        return "connect/webssh";
    }
}
