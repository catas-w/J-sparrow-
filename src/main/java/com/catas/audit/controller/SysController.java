package com.catas.audit.controller;


import com.catas.audit.common.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Description: 所有页面跳转视图
 */
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

    // ssh日志
    @RequestMapping("/session-log")
    public String sessionLog() {
        return "audit/session-log";
    }

    // 用户管理
    @RequestMapping("/admin/users")
    public String userInfo() {
        return "admin/users";
    }

    // 主机信息管理
    @RequestMapping("/admin/hosts")
    public String HostInfo() {
        return "admin/hosts";
    }

    // 主机登录账户
    @RequestMapping("/admin/account")
    public String sshAccount() {
        return "admin/account";
    }

    // bind host
    @RequestMapping("/admin/bind-host")
    public String bindHost() {
        return "admin/bindhost";
    }

    // host group
    @RequestMapping("/admin/host-group")
    public String hostGroup() {
        return "admin/hostGroup";
    }

    // idc
    @RequestMapping("/admin/idc")
    public String idc() {
        return "admin/idc";
    }

    // schedule plan
    @RequestMapping("/glimmer")
    public String glimmer() {
        return "glimmer/list";
    }

    // schedule log
    @RequestMapping("/glimmer/log")
    public String scheduleLogPage() {
        return "glimmer/logs";
    }

    // 计划详情页
    @RequestMapping("/glimmer/detail/{id}")
    public String scheduleDetail(@PathVariable("id") Integer planId) {
        return "glimmer/plan-detail";
    }

    // 多任务
    @RequestMapping("/glimmer/multi/cmd")
    public String multiTaskPage() {
        return "glimmer/multi-cmd";
    }

    // 多任务日志
    @RequestMapping("/glimmer/multi/log")
    public String multiTaskLog() {
        return "glimmer/multi-logs";
    }

    // 日志详情页
    @RequestMapping("/glimmer/multi/log/detail/{id}")
    public String multiLogDetail(@PathVariable("id") Integer logId) {
        return "glimmer/multi-logs-detail";
    }
}
