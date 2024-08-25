import com.sun.deploy.util.SyncAccess;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    // static Lock lock = new ReentrantLock();
    // 模拟被读写的数据
    private static int value = 5;
    // 创建读写锁
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // 读锁
    static Lock readLock = readWriteLock.readLock();
    // 写锁
    static Lock writeLock = readWriteLock.writeLock();

    // 模拟读操作
    public static void read(Lock lock) {
        try {
            // 上锁
            lock.lock();
            Thread.sleep(1000);
            // 读数据
            System.out.println("read: " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 模拟写操作
    public static void write(Lock lock, int v) {
        try {
            // 上锁
            lock.lock();
            Thread.sleep(1000);
            // 写数据
            value = v;
            System.out.println("write: " + v);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable readR = () -> read(readLock);
        Runnable writeR = () -> write(writeLock, ((int) Math.random() * 10 + 1));

        // 开启写线程
        for (int i = 0; i < 18; i++) {
            new Thread(readR).start();
        }
        // 开启读线程
        for (int j = 0; j < 2; j++) {
            new Thread(writeR).start();
        }
    }
}
