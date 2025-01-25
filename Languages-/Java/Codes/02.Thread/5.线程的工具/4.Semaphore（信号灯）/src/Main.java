import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 允许一个线程同时执行
        Semaphore s = new Semaphore(1);

        new Thread(() -> {
            try {
                // 获取信号
                s.acquire();
                System.out.println("t1 running");
                Thread.sleep(5000);
                // 释放信号
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 保证t1先启动
        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                // 阻塞，只有等5s后t1释放了信号后，t2才能得到执行
                s.acquire();
                System.out.println("t2 running");
                // 释放信号
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 结果：
        // t1 running
        // 等5s过后
        // t2 running
    }
}
