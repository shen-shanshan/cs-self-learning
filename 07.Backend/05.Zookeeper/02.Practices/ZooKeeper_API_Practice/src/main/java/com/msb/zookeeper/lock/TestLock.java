package com.msb.zookeeper.lock;

import com.msb.zookeeper.config.ZKUtils;
import org.apache.zookeeper.ZooKeeper;

public class TestLock {

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

    public void lock() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    // 每一个线程代表一个客户端
                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);
                    String threadName = Thread.currentThread().getName();
                    watchCallBack.setThreadName(threadName);
                    // 抢锁
                    watchCallBack.tryLock();
                    // 办业务
                    System.out.println(threadName + "working...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 释放锁
                    watchCallBack.unLock();
                }
            }.start();
        }
        while (true) {

        }
    }
}
