package com.test.demo.configuration.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class ListenerConfig {

    @PreDestroy
    public void destory() {
    }

    //servlet启动监听器
    @Bean
    public ServletListenerRegistrationBean servletContextListener(){
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean();
        slrb.setListener(new ServletContextListener(){
            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                System.out.println("关闭servlet,此次应用总访问量：" +sce.getServletContext().getAttribute("count"));
            }
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                System.out.println("启动servlet,此次启动访问量：0");
                sce.getServletContext().setAttribute("count", 0);
                sce.getServletContext().setAttribute("lineCount", 0);
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
