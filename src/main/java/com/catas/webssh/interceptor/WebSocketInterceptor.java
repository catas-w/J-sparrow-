package com.catas.webssh.interceptor;

import com.catas.audit.common.ActiveUser;
import com.catas.webssh.constant.ConstantPool;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {


        // 获取当前用户信息
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (activeUser == null) {
            return false;
        }

        Integer userId = activeUser.getUserInfo().getId();

        //生成一个UUID
        String uuid = UUID.randomUUID().toString() + "-" + userId;
        //将uuid放到websocketsession中
        map.put(ConstantPool.USER_UUID_KEY, uuid);
        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
