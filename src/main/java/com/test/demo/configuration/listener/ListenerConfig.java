package com.test.demo.configuration.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class ListenerConfig {

    //servlet启动监听器
    @Bean
    public ServletListenerRegistrationBean servletContextListener(){
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean();
        slrb.setListener(new ServletContextListener(){
            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                System.out.println("contextDestroyed" + "," + new Date());
                File file = new File("D:\\count.txt");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(sce.getServletContext().getAttribute("count").toString());
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                System.out.println("contextInitialized" + "," + new Date());
                File file = new File("D:\\Download\\count.txt");
                if (file.exists()) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
                        BufferedReader bReader = new BufferedReader(inputStreamReader);
                        Integer count = Integer.valueOf(bReader.readLine());
                        System.out.println("历史访问次数：" + count);
                        sce.getServletContext().setAttribute("count", count);
                        sce.getServletContext().setAttribute("lineCount", 0);
                        bReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return slrb;
    }

    //servlet请求监听器
    @Value(value="${servlet.request.listener.unlistener-url}")
    String unListenerUrl[];
    @Bean
    public ServletListenerRegistrationBean servletRequestListener(){
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean();
        slrb.setListener(new ServletRequestListener(){
            @Override
            public void requestDestroyed(ServletRequestEvent sce) {
                HttpServletRequest request = (HttpServletRequest)sce.getServletRequest();
                String url = request.getRequestURI();
                if(!Arrays.asList(unListenerUrl).contains(url)) {
                    Integer count = (Integer) sce.getServletContext().getAttribute("count");
                    System.out.println("当前访问次数：：" + count);
                }
            }

            @Override
            public void requestInitialized(ServletRequestEvent sce) {
                HttpServletRequest request = (HttpServletRequest)sce.getServletRequest();
                String url = request.getRequestURI();
                if(!Arrays.asList(unListenerUrl).contains(url)) {
                    System.out.println("----发向" + url + "请求被初始化----");
                    System.out.println("requestInitialized" + "," + new Date());
                    Integer count = (Integer) sce.getServletContext().getAttribute("count");
                    System.out.println("历史访问次数：：" + count);
                    count++;
                    sce.getServletContext().setAttribute("count", count);
                }
            }
        });
        return slrb;
    }

    //HttpSession监听器
    @Bean
    public ServletListenerRegistrationBean httpSessionListener(){
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean();
        slrb.setListener(new HttpSessionListener(){
            @Override
            public void sessionCreated(HttpSessionEvent hse) {
                System.out.println("sessionCreated" + "," + new Date());
                Integer count = (Integer)hse.getSession().getServletContext().getAttribute("lineCount");
                count++;
                System.out.println("新上线一人，当前在线人数有： " + count + " 个");
                hse.getSession().getServletContext().setAttribute("lineCount", count);
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent hse) {
                System.out.println("sessionDestroyed" + "," + new Date());
                Integer count = (Integer)hse.getSession().getServletContext().getAttribute("lineCount");
                count--;
                System.out.println("一人下线，当前在线人数: " + count + " 个");
                hse.getSession().getServletContext().setAttribute("lineCount", count);
            }
        });
        return slrb;
    }
}
