package com.javbus.monitor;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zy-Ma
 */
public class Client {
    private ZooKeeper zkClient;

    private void getZk() throws IOException {
        zkClient = new ZooKeeper("bigdata01:2181,bigdata02:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    monitor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void monitor() throws InterruptedException, KeeperException {
        List<String> children = zkClient.getChildren("/monitor", true);
        ArrayList<String> hostnames = new ArrayList<>();
        for (String child : children) {
            byte[] data = zkClient.getData("/monitor/" + child, false, null);
            hostnames.add(new String(data));
        }
        System.out.println(hostnames);
    }
    

    private void running() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        Client client = new Client();
        // 获取客户端
        client.getZk();
        // 客户端对服务器变化进行监听
        client.monitor();
        // 客户端持续运行 进行监听
        client.running();
    }
}
