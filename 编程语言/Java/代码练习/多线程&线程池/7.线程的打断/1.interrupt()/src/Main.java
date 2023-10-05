import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // 在这里实现中断后的处理
                System.out.println("t1 is interrupted!");
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });
        t1.start();

        TimeUnit.SECONDS.sleep(2);

        t1.interrupt();

        // 结果：
        // t1 is interrupted!
        // false
    }
}
