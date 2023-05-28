import java.util.concurrent.TimeUnit;

public class Main {
    private static volatile boolean running = true;

    private static void m() {
        System.out.println("m start!");
        while (running) {
        }
        System.out.println("m end!");
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            Main.m();
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        running = false;
    }
}
