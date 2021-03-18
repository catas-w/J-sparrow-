package com.catas.webssh.config;

import com.catas.webssh.interceptor.WebSocketInterceptor;
import com.catas.webssh.websocket.WebSSHWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
* @Description: websocket配置
* @Author: NoCortY
* @Date: 2020/3/8
*/
@Configuration
@EnableWebSocket
public class WebSSHWebSocketConfig implements WebSocketConfigurer{

    @Autowired
    WebSSHWebSocketHandler webSSHWebSocketHandler;

    @Autowired
    WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //socket通道
        //指定处理器和路径
        webSocketHandlerRegistry.addHandler(webSSHWebSocketHandler, "/webssh")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
