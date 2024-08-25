package com.msb.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

// 工具类
public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    ZooKeeper zk;

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    Myconf conf;

    public Myconf getConf() {
        return conf;
    }

    public void setConf(Myconf conf) {
        this.conf = conf;
    }

    // getData callBack
    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (bytes != null) {
            String data = new String(bytes);
            conf.setConf(data);
            cc.countDown();
        }
    }

    // getStat callBack
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null) {
            zk.getData("/AppConf", this, this, "sdf");
        }
    }

    // watch callBack
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf", this, this, "sdf");
                break;
            case NodeDeleted:
                // 清空节点的数据
                conf.setConf("");
                cc = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/AppConf", this, this, "sdf");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }

    CountDownLatch cc = new CountDownLatch(1);

    public void await() {
        zk.exists("/AppConf", this, this, "ABC");
        try {
            // 阻塞，等待 exits 获取完数据后才可以打印
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
