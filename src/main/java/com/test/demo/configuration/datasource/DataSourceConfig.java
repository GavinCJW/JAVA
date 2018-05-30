package com.test.demo.configuration.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    //数据源1
    @Bean(name="dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    //数据源2
    @Bean(name="dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("dynamicDataSource")
    @DependsOn({"dataSource","dataSource1","dataSource2"})
    public DataSource dynamicDataSource() {
        AbstractRoutingDataSource dynamicRoutingDataSource = new AbstractRoutingDataSource(){
            @Override
            protected Object determineCurrentLookupKey() {
                return DataSourceContextHolder.getDB();
            }
        };
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceKey.Default, dataSource());
        dataSourceMap.put(DataSourceKey.DB1, dataSource1());
        dataSourceMap.put(DataSourceKey.DB2, dataSource2());

        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSource());
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        return dynamicRoutingDataSource;
    }

}