package com.test.demo.configuration.filter;

import com.test.demo.utils.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Configuration
public class FilterConfig {

    //跨域过滤器
    @Value(value="${cors.origin}")
    String origin[];
    @Value(value="${cors.header}")
    String header[];
    @Value(value="${cors.method}")
    String method[];
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        for (String key:origin)
            corsConfiguration.addAllowedOrigin(key);
        for (String key:header)
            corsConfiguration.addAllowedHeader(key);
        for (String key:method)
            corsConfiguration.addAllowedMethod(key);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 4
        return new CorsFilter(source);
    }

    //自定义过滤器
    @Value(value="${my.filter.unfilter-url}")
    String unFilterUrl[];
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        Filter testFilter = new Filter(){
            @Override
            public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest request= (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;

                String url = request.getRequestURI();

                if(!Arrays.asList(unFilterUrl).contains(url)){
                    System.out.println("过滤器实现-------" + url + "------------" + IpUtil.getIpAddr(request) + "--------");

                    /*RequestDispatcher dispatcher = request.getRequestDispatcher("/mybatis");
                    dispatcher.forward(request, response);*/

                    //return ;
                }
                filterChain.doFilter(request,response);
            }

            @Override
            public void destroy() {

            }
        };   //new过滤器

        filterRegistrationBean.setFilter(testFilter);       //set
        //filterRegistrationBean.setUrlPatterns(urlPatterns);     //set
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("paramName", "paramValue");
        filterRegistrationBean.setName("myFilter");

        return filterRegistrationBean;
    }
}


