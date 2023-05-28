import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) {
                    System.out.println("t1 is interrupted!");
                    System.out.println(Thread.interrupted());
                }
            }
        });
        t1.start();

        TimeUnit.SECONDS.sleep(2);

        t1.interrupt();

        // 此时线程t1的中断标志位应该是true（被打断了）
        // 但是这里打印的是false
        // 说明这里打印的是当前线程（main）的中断标志位
        System.out.println(t1.interrupted());

        // 结果
        // false（主线程的）
        // t1 is interrupted!
        // false（线程t1的）
    }
}
