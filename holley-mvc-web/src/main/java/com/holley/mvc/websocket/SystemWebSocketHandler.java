package com.holley.mvc.websocket;

import java.util.concurrent.TimeUnit;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.holley.mvc.service.CommonService;

public class SystemWebSocketHandler implements WebSocketHandler {

    private CommonService commonService;

    public SystemWebSocketHandler() {
        super();
    }

    public SystemWebSocketHandler(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("ConnectionEstablished");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("content1:" + message);
        System.out.println("content2:" + message.getPayload());
        System.out.println("content3:" + JSON.parseObject(message.getPayload().toString()));
        TimeUnit.SECONDS.sleep(5);
        session.sendMessage(new TextMessage("服务器信息"));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("error");
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("close...");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
