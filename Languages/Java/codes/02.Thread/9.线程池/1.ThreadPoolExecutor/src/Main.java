import javafx.concurrent.Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 创建
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 4,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        // 参数:
        // corePoolSize:2
        // maximumPoolSize:4
        // keepAliveTime:60
        // keepAliveTime单位:秒
        // 等待队列容量:4
        // 线程工厂：默认
        // 拒绝策略：丢弃最老的线程

        for (int i = 0; i < 9; i++) {
            tpe.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " running...");
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);

        System.out.println(tpe.getQueue());

        // 关闭
        tpe.shutdown();

        // 结果：
        // i = 2
        // pool-1-thread-1 running...
        // pool-1-thread-2 running...
        // []

        // i = 5
        // pool-1-thread-2 running...
        // pool-1-thread-1 running...
        // [Main$1@14ae5a5, Main$1@7f31245a, Main$1@6d6f6e28]

        // i = 7
        // pool-1-thread-2 running...
        // pool-1-thread-1 running...
        // pool-1-thread-3 running...
        // [Main$1@7f31245a, Main$1@6d6f6e28, Main$1@135fbaa4, Main$1@45ee12a7]

        // i = 9
        // pool-1-thread-2 running...
        // pool-1-thread-4 running...
        // pool-1-thread-3 running...
        // pool-1-thread-1 running...
        // [Main$1@6d6f6e28, Main$1@135fbaa4, Main$1@45ee12a7, Main$1@330bedb4]
    }
}
