import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService es = Executors.newCachedThreadPool();
        // 提交任务
        es.execute(() -> {
            System.out.println("thread start!!!");
        });
        // 关闭线程池
        es.shutdown();
    }
}
