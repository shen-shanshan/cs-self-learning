import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                // 阻塞
                LockSupport.park();
                System.out.println("thread running...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t");
        t.start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println(t.getName() + ": " + t.getState());

        // 解锁
        LockSupport.unpark(t);

        // 结果：
        // t: WAITING
        // thread running...
    }
}
