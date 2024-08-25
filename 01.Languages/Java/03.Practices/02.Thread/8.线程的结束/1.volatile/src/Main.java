import java.util.concurrent.TimeUnit;

public class Main {
    // 创建控制变量
    public static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                int i = 0;
                while (running) {
                    i++;
                    TimeUnit.SECONDS.sleep(1);
                }
                System.out.println("t1 stop!, i = " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        TimeUnit.SECONDS.sleep(5);

        // 结束线程的执行
        running = false;

        // 结果：
        // t1 stop!, i = 5
    }
}
