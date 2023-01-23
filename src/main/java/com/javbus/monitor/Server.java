package com.javbus.monitor;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author zy-Ma
 */
public class Server {

    private ZooKeeper zkClient;

    public void getZk() throws IOException {
         zkClient = new ZooKeeper("bigdata01:2181,bigdata02:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }

    public void register(String hostname) throws InterruptedException, KeeperException {
        String node = zkClient.create("/monitor/" + hostname, hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(node + " is online");
    }

    public void running() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }


  public static void main(String[] args) throws IOException, InterruptedException, KeeperException {


      Server server = new Server();
      // 获取客户端
      server.getZk();
      // 服务器上线注册
      server.register(args[0]);
      // 服务器运行
      server.running();
  }


}
