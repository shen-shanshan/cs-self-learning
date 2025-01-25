import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SellTickets implements Runnable {
    private int tickets = 500;
    // 锁对象，可以是任意对象，多个线程必须使用同一个锁。
    private Object obj = new Object();
    private Lock lock = new ReentrantLock();

    public void run() {
        while (true) {
            // 方式一：同步代码块
            // 采用 synchronized 不需要用户去手动释放锁。
            // 当 synchronized 方法或者 synchronized 代码块执行完之后，系统会自动让线程释放对锁的占用。
            /*synchronized (obj) {
                if (tickets > 0) {
                    try {
                        System.out.println(Thread.currentThread().getName() + "正在卖第" + tickets + "张票");
                        tickets--;
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            // 方式二：Lock 接口
            // 必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象。
            // 死锁：多个线程因争夺资源而互相等待的现象。
            try {
                // 上锁
                lock.lock();
                // 处理任务
                if (tickets > 0) {
                    System.out.println(Thread.currentThread().getName() + "正在卖第" + tickets + "张票");
                    tickets--;
                }
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
