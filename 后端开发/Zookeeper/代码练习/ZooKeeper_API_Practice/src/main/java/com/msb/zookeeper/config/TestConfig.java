package com.msb.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

// 测试类
public class TestConfig {

    ZooKeeper zk;

    public void conn() {
        zk = ZKUtils.getZk();
    }

    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getConf() {

        // 它既是 watch ，又是 callback
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        // 用于获取数据
        Myconf myconf = new Myconf();
        watchCallBack.setConf(myconf);

        // 异步执行，不会阻塞，代码瞬间往下走
        // 这里的 / 就相当于 /testConf
        zk.exists("/AppConf", watchCallBack, watchCallBack, "ABC");

        // 阻塞，等待 exits 获取完数据后才可以打印
        watchCallBack.await();
        // 打印数据
        while (true) {
            if (myconf.getConf().equals("")) {
                System.out.println("无配置文件");
                watchCallBack.await();
            } else {
                System.out.println(myconf.getConf());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
