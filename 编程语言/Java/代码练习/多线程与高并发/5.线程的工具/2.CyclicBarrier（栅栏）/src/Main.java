import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        // 设置栅栏
        CyclicBarrier barrier = new CyclicBarrier(20, new Runnable() {
            @Override
            public void run() {
                // 每来20个线程打开一次栅栏，一共会打印5次
                System.out.println("打开栅栏");
            }
        });

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                // 阻塞，等待的线程数达到parties时打开栅栏
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
