package com.test.demo.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.test.demo.configuration.datasource.DynamicDataSource;
import com.test.demo.configuration.datasource.DynamicDataSourceContextHolder;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    //如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    private ConversionService conversionService = new DefaultConversionService();
    private PropertyValues dataSourcePropertyValues;

    // 默认数据源
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

    //加载多数据源配置
    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("DynamicDataSourceRegister.setEnvironment()");
        initDefaultDataSource(environment);
        initCustomDataSources(environment);
    }



    /**

     * 加载主数据源配置.

     * @param env

     */
    private static final Object SPRING_DATASOURCE = "spring.datasource.";
    private void initDefaultDataSource(Environment env){

        // 读取主数据源
        String key[] = {"type","driverClassName","url","username","password"};
        //创建数据源;

        defaultDataSource = buildDataSource(new HashMap<String ,Object>(){
            {
                for (String k : key){
                    put(k,env.getProperty(SPRING_DATASOURCE+k));
                }
            }
        });
        dataBinder(defaultDataSource, env);

    }



    /**

     * 初始化更多数据源

     *

     * @author SHANHY

     * @create 2016年1月24日

     */
    @Value(value="${custom.datasource.names}")
    String customs[];
    private static final Object CUSTOM_DATASOURCE = "custom.datasource.";
    private void initCustomDataSources(Environment env) {

        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        String key[] = {"type","driverClassName","url","username","password"};
        for (String custom : customs) {// 多个数据源
            DataSource ds = buildDataSource(new HashMap<String ,Object>(){
                {
                    for (String k : key){
                        put(k,env.getProperty(CUSTOM_DATASOURCE+custom+"."+k));
                    }
                }
            });
            customDataSources.put(custom, ds);
            dataBinder(ds, env);
        }

    }



    /**

     * 创建datasource.

     * @param dsMap

     * @return

     */

    @SuppressWarnings("unchecked")

    public DataSource buildDataSource(Map<String, Object> dsMap) {
        Object type = dsMap.get("type");
        if (type == null){
            type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
        }

        Class<? extends DataSource> dataSourceType;
        try {
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dsMap.get("driverClassName").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
            DataSourceBuilder factory =   DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**

     * 为DataSource绑定更多数据

     * @param dataSource

     * @param env

     */
    private void dataBinder(DataSource dataSource, Environment env){


        /*RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);

        dataBinder.setConversionService(conversionService);

        dataBinder.setIgnoreNestedProperties(false);//false

        dataBinder.setIgnoreInvalidFields(false);//false

        dataBinder.setIgnoreUnknownFields(true);//true*/



        if(dataSourcePropertyValues == null){
            String key[] = {"type","driverClassName","url","username","password"};
            Map<String, Object> values = new HashMap<String, Object>(){
                {
                    for (String k : key){
                        put(k,env.getProperty(SPRING_DATASOURCE+"."+k));
                    }
                }
            };
            // 排除已经设置的属性
            for (String k : key){
                values.remove(k);
            }
            dataSourcePropertyValues = new MutablePropertyValues(values);

        }

        Binder.get(env)
                .bind("spring.datasource", DataSource.class)
                .orElse(null);
        //dataBinder.bind(dataSourcePropertyValues);



    }





    @Override

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("DynamicDataSourceRegister.registerBeanDefinitions()");
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");

        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();

        //添加属性：AbstractRoutingDataSource.defaultTargetDataSource
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }



}