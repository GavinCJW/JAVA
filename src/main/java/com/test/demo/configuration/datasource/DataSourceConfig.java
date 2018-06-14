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

@Configuration
public class DataSourceConfig {

    //主数据源
    @Bean(name=DataSourceKey.Default)
    @ConfigurationProperties(prefix = DataSourceKey.Default)
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    //数据源1
    @Bean(name=DataSourceKey.DB1)
    @ConfigurationProperties(prefix = DataSourceKey.DB1)
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    //数据源2
    @Bean(name=DataSourceKey.DB2)
    @ConfigurationProperties(prefix = DataSourceKey.DB2)
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("dynamicDataSource")
    @DependsOn({DataSourceKey.Default,DataSourceKey.DB1,DataSourceKey.DB2})
    public DataSource dynamicDataSource() {
        AbstractRoutingDataSource dynamicRoutingDataSource = new AbstractRoutingDataSource(){
            @Override
            protected Object determineCurrentLookupKey() {
                return DataSourceContextHolder.getDB();
            }
        };
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSource());
        dynamicRoutingDataSource.setTargetDataSources(new HashMap<Object, Object>(){
            {
                put(DataSourceKey.Default, dataSource());
                put(DataSourceKey.DB1, dataSource1());
                put(DataSourceKey.DB2, dataSource2());
            }
        });

        return dynamicRoutingDataSource;
    }

}