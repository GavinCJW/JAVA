package com.test.demo.configuration.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private    String adderss;

    @Value("${zookeeper.timeout}")
    private  int timeout;


    @Bean
    public ZooKeeper zkClient(){
        ZooKeeper zooKeeper=null;
        try {
            zooKeeper = new ZooKeeper(adderss, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return  zooKeeper;
    }

    @Bean
    public CuratorFramework cfClient() {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(adderss, new ExponentialBackoffRetry(timeout,3));
        curator.start();
        return curator;
    }
}
