package com.javbus;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ZkDemo {

    private ZooKeeper client;
    private ZooKeeper watcherClient;


    @Before
    public void getClient() throws IOException {

        /**
         * 参数1：连接字符串 主机:端口
         * 参数2：超时时间
         * 参数3：监听
         * */
         client = new ZooKeeper("bigdata01:2181,bigdata02:2181", 5000, null);
    }

    @Test
    public void getWatchClient() throws IOException, InterruptedException, KeeperException {

        /**
         * 当zookeeper对象创建的时候添加监听器 注册监听的时候也添加了监听器
         * 优先触发的是注册监听的时候的监听
         * */
        watcherClient = new ZooKeeper("bigdata01:2181,bigdata02:2181", 5000, new Watcher() {
            //当监听触发的时候的回调方法
            @Override
            public void process(WatchedEvent event) {
                // TODO Auto-generated method stub
                System.out.println("this is zk's watcher");
                String path = event.getPath();
                Event.EventType type = event.getType();
                Event.KeeperState state = event.getState();
                System.out.println(path+"\t"+type+"\t"+state+"\t");


                //在每次触发完监听后 进行重复注册一次 实现循环监听
                try {
                    watcherClient.getChildren("/sanguo", true);
                } catch (KeeperException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        /**
         * 注册监听第二个参数的两种形式
         * 第一种：传入watcher对象或者null
         * 当传入watcher对象的时候 代表注册了监听 监听触发时会响应这个watcher的process方法
         * 当传入null的时候 代表不添加监听
         * 第二种：传入一个boolean类型的值
         * 当传入true 代表使用创建zk对象的时候的watcher
         * 当传入false 代表不使用创建zk对象的时候的watcher
         * */

        watcherClient.getChildren("/sanguo", true);
        Thread.sleep(Integer.MAX_VALUE);
        watcherClient.close();
    }

    @Test
    public void createNode() throws InterruptedException, KeeperException {
        /**
         * 参数1：节点路径
         * 参数2：节点数据
         * 参数3：节点权限
         * 参数4：节点类型
         * 返回创建成功的节点路径
         * */
        String sanguo = client.create("/sanguo", "207".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(sanguo);
    }


    @Test
    public void deleteNode() throws InterruptedException, KeeperException {
        //删除节点
        client.delete("/sanguo",-1);
    }

    @Test
    public void updateNode() throws InterruptedException, KeeperException {
        //修改节点内容 返回值是当前节点的详细信息
        Stat stat = client.setData("/sanguo", "208".getBytes(), -1);
        System.out.println(stat);
    }

    @Test
    public void getNodeData() throws InterruptedException, KeeperException {
        //获取节点内容 有监听器
        String data = new String(client.getData("/sanguo", null, null));
        System.out.println(data);
    }

    @Test
    public void getClildNode() throws InterruptedException, KeeperException {
        //获取子节点
        List<String> children = client.getChildren("/", null);
        System.out.println(children);
    }

    @Test
    public void judgeNode() throws InterruptedException, KeeperException {
        //判断节点是否存在 存在返回节点的详细信息 不存在返回null
        Stat exists = client.exists("/wuguo", null);
        System.out.println(exists);
    }
}
