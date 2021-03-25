package com.catas.audit.controller;


import com.catas.audit.common.DataGridView;
import com.catas.audit.entity.Sessionlog;
import com.catas.audit.service.ISessionlogService;
import com.catas.audit.vo.SessionLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sshlog")
public class SessionLogController {

    @Autowired
    private ISessionlogService sessionlogService;

    @Value("${ssh.log-path:#{null}}")
    private String logPath;

    @RequestMapping("/get-all")
    public DataGridView getAllLogInfo(SessionLogVo sessionLogVo) {

        List<Map<String, Object>> aLlSessionLogInfo = sessionlogService.getALlSessionLogInfo(sessionLogVo);
        return new DataGridView((long) aLlSessionLogInfo.size(), aLlSessionLogInfo);
    }

    // 下载日志文件
    @RequestMapping("/download/{id}")
    public void downloadSSHLog(@PathVariable("id") Integer logId, HttpServletResponse response) {
        Sessionlog sessionlog = sessionlogService.getById(logId);
        String tag = sessionlog.getSessionTag();
        File logFile = Path.of(this.logPath, tag + ".log").toFile();
        if (!logFile.exists()) {
            return;
        }
        try{
            FileInputStream inputStream = new FileInputStream(logFile);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String diskfilename = "log_" + logId + ".txt";
            response.setContentType("txt/plain");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + (data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();

            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
