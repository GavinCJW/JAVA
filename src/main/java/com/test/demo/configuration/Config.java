package com.test.demo.configuration;

import com.test.demo.utils.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class Config {

    //webSocket端点实例
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

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
            public void init(FilterConfig filterConfig) throws ServletException {

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
        slrb.setListener(new ServletRequestListener (){
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
