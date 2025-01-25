import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);

        Thread t1 = new Thread(() -> {
            try {
                // 阻塞，等待门闩打开后才能继续执行
                latch.await();
                System.out.println("门闩打开");
                System.out.println("t1 start");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println("t2 start");
            for (int i = 5; i > 0; i--) {
                // 当把latch减到0后打开门闩
                System.out.println("count = " + i);
                latch.countDown();
            }
        });

        t1.start();
        t2.start();

        // 运行结果：
        // t2 start
        // count = 5
        // count = 4
        // count = 3
        // count = 2
        // count = 1
        // 门闩打开
        // t1 start
    }
}
