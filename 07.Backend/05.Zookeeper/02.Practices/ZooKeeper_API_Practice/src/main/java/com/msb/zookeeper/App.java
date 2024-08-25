package com.msb.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

// ZooKeeper 客户端
// 有 session 的概念，没有连接池的概念
public class App {
    public static void main(String[] args) throws Exception {

        // 保证线程安全，解决 zk 创建的异步问题
        final CountDownLatch cd = new CountDownLatch(1);

        // watch：观察，回调
        // watch 的注册只发生在“读”类型调用，如：get、exists ...
        // 第一类：new zk 的时候传入的 watch
        // 它只和客户端对应的 session 有关，与 path、node 无关
        // sessionTimeout：超时时间，当客户端断开连接后，对应的 session 及其临时节点还可以存活 3000ms
        final ZooKeeper zk = new ZooKeeper("192.168.150.xxx:2181,192.168.150.xxx:2181,...",
                3000, new Watcher() {
            // watch 的回调方法
            @Override
            public void process(WatchedEvent watchedEvent) {

                Event.KeeperState state = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                String path = watchedEvent.getPath();
                System.out.println("default watch:" + watchedEvent.toString());

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected");
                        // 解除线程的阻塞
                        cd.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
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
        });

        // 阻塞，只有等 cd.countDown() 执行后才解锁
        // 目的：保证连接成功后，才能继续往下执行别的代码
        cd.await();
        // 获取 zk 客户端的连接状态
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing...");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed...");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        // 创建节点
        String pathName = zk.create("/ooxx", "olddata".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        final Stat stat = new Stat();
        // 第二类 watch，专门用于监控 "/ooxx"，与其 path、node 相对应
        byte[] node = zk.getData("/ooxx", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("getData watch:" + watchedEvent.toString());
                try {
                    // true 表示将 default watch 重新注册到 "/ooxx" 上
                    // default watch 表示 new zk 时使用的那个 watch
                    zk.getData("/ooxx", true, stat);
                    // this 表示继续使用当前 watch 进行监控
                    zk.getData("/ooxx", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println(new String(node));

        // 触发 watch
        Stat stat1 = zk.setData("/ooxx", "newdata".getBytes(), 0);
        Stat stat2 = zk.setData("/ooxx", "newdata01".getBytes(), stat1.getVersion());

        // 找到 ZooKeeper 中的 conf 文件夹下的 log4j.properties 文件
        // 将其加入到项目的 resources 文件夹下，实现日志的打印

        // 回调是异步执行，线程不会在这里阻塞住
        System.out.println("async start");
        zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("call back");
                System.out.println(o.toString());
                System.out.println(new String(bytes));
            }
        }, "abc");
        System.out.println("async over");
        // 打印结果
        // async start
        // async over
        // call back
    }
}
