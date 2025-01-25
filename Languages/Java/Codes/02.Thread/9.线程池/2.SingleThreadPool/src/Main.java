import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Executors相当于线程池的工厂，用于创建线程池
        ExecutorService service = Executors.newSingleThreadExecutor();
        // ExecutorService service = Executors.newCachedThreadPool();
        // ExecutorService service = Executors.newFixedThreadPool(10);
        // ExecutorService service = Executors.newScheduledThreadPool();

        for (int i = 0; i < 5; i++) {
            final int j = i;
            service.execute(() -> {
                System.out.println(j);
            });
        }
    }
}
