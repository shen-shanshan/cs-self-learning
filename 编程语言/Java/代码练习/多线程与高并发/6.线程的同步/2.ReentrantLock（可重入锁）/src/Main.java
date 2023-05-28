import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        // 创建锁
        Lock lock = new ReentrantLock();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // 上锁
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " lock");
                    System.out.println(Thread.currentThread().getName() + " running...");
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放锁
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " unlock");
                }
            }
        };

        new Thread(r, "t1").start();
        new Thread(r, "t2").start();

        // 结果：
        // t1 lock
        // t1 running...
        // t1 unlock
        // t2 lock
        // t2 running...
        // t2 unlock
    }
}
