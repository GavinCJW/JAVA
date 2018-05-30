package com.test.demo.configuration.interceptor;

import com.test.demo.utils.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;

@Configuration
public class InterceptorConfig {

    //url拦截器
    @Value(value="${url.interceptor.white-list}")
    String whiteList[];
    @Bean
    public WebMvcConfigurer urlInterceptor(){
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor(){
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
                        String ip = IpUtil.getIpAddr(request);

                        if (Arrays.asList(whiteList).contains(ip)) {
                            return true;
                        } else {
                            System.out.println("urlInterceptor" + "," + "您好，ip为" + ip + ",暂时没有访问权限，请联系管理员开通访问权限。");
                            response.setHeader("Content-type","text/html;charset=UTF-8");//向浏览器发送一个响应头，设置浏览器的解码方式为UTF-8
                            String data = "您好，ip为" + ip + ",暂时没有访问权限，请联系管理员开通访问权限。";
                            OutputStream stream = response.getOutputStream();
                            stream.write(data.getBytes("UTF-8"));
                            return false;
                        }
                    }
                }).addPathPatterns("/**");
            }
        };
    }
}
