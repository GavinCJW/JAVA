package com.test.demo.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.poi.ss.formula.functions.T;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class ZookeeperUtil {

    @Autowired
    private ZooKeeper zkClient;

    @Autowired
    CuratorFramework curator;

    public void getDistributedLock(int time ,Object param , Function<Object,Object> func){
        InterProcessMutex mutex = new InterProcessMutex(curator, "/lock");
        try {
            mutex.acquire(time, TimeUnit.SECONDS);
            func.apply(param);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                mutex.release();
            }catch (Exception e){}
        }
    }

    public Stat exists(String path, boolean watch){
        try {
            return zkClient.exists(path,watch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stat exists(String path, Watcher watcher){
        try {
            return zkClient.exists(path,watcher);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean createNode(String path, String data ,CreateMode createMode){
        try {
            zkClient.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createPersistentNode(String path, String data){
        return createNode(path,data,CreateMode.PERSISTENT);
    }

    public boolean createPersistentSequentialNode(String path, String data){
        return createNode(path,data,CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public boolean createEphemeralNode(String path, String data){
        return createNode(path,data,CreateMode.EPHEMERAL);
    }

    public boolean createEphemeralSequentialNode(String path, String data){
        return createNode(path,data,CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public boolean updateNode(String path, String data){
        try {
            zkClient.setData(path,data.getBytes(),-1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNode(String path){
        try {
            zkClient.delete(path,-1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getChildren(String path) throws KeeperException, InterruptedException{
        List<String> list = zkClient.getChildren(path, false);
        return list;
    }

    public String getData(String path,Watcher watcher){
        try {
            return  new String(zkClient.getData(path,watcher,new Stat()));
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    //存在惊群现象，分布式时请使用getDistributedLock
    private String LOCK_PATH = "/zk_lock_path";
    public boolean lock(int timeout , int interval) {
        long time = System.currentTimeMillis();
        try {
            if(exists(LOCK_PATH,false) == null){
                createEphemeralNode(LOCK_PATH, "");
                System.out.println("success to acquire lock");
                return true;
            }else{
                int count = 0;
                while ((System.currentTimeMillis() - time) < TimeUnit.SECONDS.toMillis(timeout)){
                    Thread.sleep(TimeUnit.SECONDS.toMillis(interval));
                    if(exists(LOCK_PATH,false) == null){
                        createEphemeralNode(LOCK_PATH, "");
                        System.out.println("success to acquire lock");
                        return true;
                    }
                    System.out.println("the " + (count++) + " times try to acquire lock");
                }
            }
            System.out.println("try to acquire lock fails timeout");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unlock() {
        try {
            deleteNode(LOCK_PATH);
            System.out.println("release the lock");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
