//import java.util.concurrent.CountDownLatch;
//
//public class Main {
//    private static long n = 0L;
//
//    public static void main(String[] args) throws InterruptedException {
//        Thread[] t = new Thread[100];
//        CountDownLatch latch = new CountDownLatch(t.length);
//
//        for (int i = 0; i < t.length; i++) {
//            t[i] = new Thread(() -> {
//                for (int j = 0; j < 10000; j++) {
//                    n++;
//                }
//                latch.countDown();
//            });
//        }
//
//        for (int k = 0; k < t.length; k++) {
//            t[k].start();
//        }
//
//        // 阻塞，等待所有线程执行完毕后再打印n的值
//        latch.await();
//        System.out.println(n);
//    }
//}

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static CountDownLatch latch = new CountDownLatch(100);
    // 设置初始值为0
    AtomicInteger count = new AtomicInteger(0);

    void run() {
        for (int i = 0; i < 10000; i++) {
            // count++
            count.incrementAndGet();
        }
        latch.countDown();
        System.out.println(Thread.currentThread().getName() + ":" + count);
    }

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();
        List<Thread> t = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            t.add(new Thread(m::run, "thread-" + i));
        }

        t.forEach((o) -> o.start());

        latch.await();
        System.out.println(m.count); // 1000000
    }
}