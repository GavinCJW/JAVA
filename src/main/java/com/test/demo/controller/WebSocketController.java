package com.test.demo.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketController {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("{\"content\":\"done\",\"type\":\"handshake\"}");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        String message = dealMsg(this.session,"{\"type\":\"logout\"}");
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        send(message);
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        message = dealMsg(session,message);
        //群发消息
        for (WebSocketController item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    public void sendMessage(String message) throws IOException {
        //this.session.getBasicRemote().sendText(message);
        this.session.getAsyncRemote().sendText(message);
    }
    /**
     * 群发自定义消息
     */
    public static void send(String message) {
        for (WebSocketController item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }
    /**
     *
     * @param message
     * @return
     */
    private static Map<String,String> nameList = new HashMap<>();
    public static synchronized String dealMsg(Session session , String message){
        Map<String,Object> map = (Map)JSON.parse(message);
        switch ((String)map.get("type")){
            case "login":
                nameList.put(session.getId(),(String)map.get("content"));
                map.put("user_list",nameList);
                break;
            case "logout":
                map.put("content",nameList.get(session.getId()));
                nameList.remove(session.getId());
                map.put("user_list",nameList);
                break;
            case "user":
                map.put("from",nameList.get(session.getId()));
                break;
        }
        System.out.println(map);
        return JSON.toJSONString(map);
    }
}
