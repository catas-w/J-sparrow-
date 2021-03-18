package com.catas.webssh.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description: webssh数据传输
* @Author: NoCortY
* @Date: 2020/3/8
*/

@Data
@NoArgsConstructor
public class WebSSHData {

    //操作
    private String operate;

    private String ipAddress;

    private Integer bindHostId;

    private String sslPath;

    private Integer port;

    private String username;

    private String password;

    private String command = "";

    private int cols = 80;
    private int rows = 24;
    private int width = 640;
    private int height = 480;
}
