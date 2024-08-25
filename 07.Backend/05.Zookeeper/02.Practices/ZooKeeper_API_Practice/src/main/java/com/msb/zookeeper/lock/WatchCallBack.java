package com.msb.zookeeper.lock;

import com.msb.zookeeper.config.Myconf;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// 工具类
public class WatchCallBack implements Watcher, AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    ZooKeeper zk;

    String threadName;

    String pathName;

    CountDownLatch cc = new CountDownLatch(1);

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void tryLock() {
        try {
            zk.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL, this, "abc");
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete(pathName, -1);
            System.out.println(threadName + "over work");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    // Watcher
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/", false, this, "sdf");
                break;
            case NodeDataChanged:
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

    // StringCallback
    @Override
    public void processResult(int i, String s, Object o, String s1) {
        if (s1 != null) {
            System.out.println(threadName + "create node: " + s1);
            pathName = s1;
            // 父节点（目录）是不需要 watch 的
            zk.getChildren("/", false, this, "sdf");
        }
    }

    // Children2Callback
    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        // list：能看到序列号排在自己前面的所有节点
        Collections.sort(list);
        int j = list.indexOf(pathName.substring(1));
        if (j == 0) {
            System.out.println(threadName + "i am first...");
            try {
                zk.setData("/",threadName.getBytes(),-1);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cc.countDown();
        } else {
            zk.exists("/" + list.get(j - 1), this, this, "sdf");
        }
    }

    // StatCallback
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {

    }
}
