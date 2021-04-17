package com.catas.glimmer.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.glimmer.dto.TaskDetailDto;
import com.catas.glimmer.dto.TaskLogDto;
import com.catas.glimmer.entity.TaskLog;
import com.catas.glimmer.service.IMultiTaskService;
import com.catas.glimmer.service.ITaskLogService;
import com.catas.glimmer.vo.TaskLogVo;
import com.github.sonus21.rqueue.core.RqueueMessageSender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/glimmer/multi")
public class MultiTaskController {

    @Autowired
    private RqueueMessageSender rqueueMessageSender;

    @Value("${upload.tmp}")
    private String tempDir;

    @Autowired
    private IMultiTaskService multiTaskService;

    @Autowired
    private ITaskLogService taskLogService;

    /**
     *
     * @Description: 新建批量任务
     * @return: 任务id
     */
    @RequestMapping("/cmd/create")
    public DataGridView createTask(TaskLogVo taskLogVo) {
        List<Integer> bindHostIds = taskLogVo.getBindHostIds();
        if (bindHostIds == null || bindHostIds.size() == 0) {
            return new DataGridView(Constant.ERROR, "参数不正确");
        }

        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();
        taskLogVo.setUserId(userId);
        taskLogVo.setTaskType(Constant.MULTI_TASK_CMD);
        int taskId = multiTaskService.createTask(taskLogVo);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("id", taskId);
        return new DataGridView(hashMap);
    }

    /**
     * @return 当前用户任务日志
     */
    @RequestMapping("/logs/all")
    public DataGridView getLogs(TaskLogVo taskLogVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();

        Page<TaskLog> logPage = new Page<>(taskLogVo.getPage(), taskLogVo.getLimit());
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("start_time");
        taskLogService.page(logPage, queryWrapper);

        logPage.convert(taskLog -> {
            TaskLogDto logDto = new TaskLogDto();
            BeanUtils.copyProperties(taskLog, logDto);
            return logDto;
        });
        return new DataGridView(logPage.getTotal(), logPage.getRecords());
    }

    /**
     * @return 多任务日志详情
     */
    @RequestMapping("/logs/detail/{id}")
    public DataGridView getLogDetail(@PathVariable("id") Integer taskLogId) {
        List<TaskDetailDto> taskDetails = taskLogService.getRelatedTaskDetail(taskLogId);
        return new DataGridView(taskDetails);
    }

    /**
     * @Description: 上传文件
     */
    @RequestMapping("/scp/upload")
    public ResultObj upload(@RequestParam("file") MultipartFile file) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();

        try {
            // Get the file and save it somewhere
            Path path = Paths.get(tempDir, userId +"", file.getOriginalFilename());
            File tempFile = new File(String.valueOf(path));
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            // tempFile.createNewFile();
            file.transferTo(tempFile);
            return new ResultObj(Constant.OK, "Upload success");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "上传失败");
        }
    }

    /**
     * @Description: 删除文件
     */
    @RequestMapping("/scp/remove")
    public ResultObj removeFile(@RequestParam("filename") String filename) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();
        try {
            Path path = Paths.get(tempDir, userId +"", filename);
            File toDelete = new File(String.valueOf(path));
            if (toDelete.isFile())
                toDelete.delete();
            return new ResultObj(Constant.OK, "Delete success");
        } catch (SecurityException e2) {
            e2.printStackTrace();
            return new ResultObj(Constant.ERROR, "删除失败");
        }
    }

    /**
     *
     * @Description: 新建sftp任务
     * @return 结果id
     */
    @RequestMapping("/scp/create")
    public DataGridView createMultiScp(TaskLogVo taskLogVo) {
        List<Integer> bindHostIds = taskLogVo.getBindHostIds();
        if (bindHostIds == null || bindHostIds.size() == 0) {
            return new DataGridView(Constant.ERROR, "参数不正确");
        }

        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();

        taskLogVo.setUserId(userId);
        taskLogVo.setTaskType(Constant.MULTI_TASK_SCP);
        List<String> files = taskLogVo.getFiles();
        for (int i=0; i<files.size(); i++) {
            Path path = Paths.get(tempDir, userId + "", files.get(i));
            String finalPath = path.toString();
            files.set(i, finalPath);
        }

        int taskId = multiTaskService.createTask(taskLogVo);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("id", taskId);
        return new DataGridView(hashMap);
    }
}
