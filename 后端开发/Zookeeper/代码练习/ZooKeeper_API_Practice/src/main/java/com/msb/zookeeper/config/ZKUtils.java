package com.msb.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

// ZooKeeper 配置
public class ZKUtils {

    private static ZooKeeper zk;

    // 设置 testConf 为当前 ZooKeeper 的 / 目录
    // 每一个 ZooKeeper 不能看到其它与自己的 testConf 同级的目录
    private static String address = "192.168.150.xxx:2181,.../testConf";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZk() {
        try {
            zk = new ZooKeeper(address, 1000, watch);
            watch.setCc(init);
            // 阻塞，等待 ZooKeeper 对象初始化完成后才返回
            init.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
