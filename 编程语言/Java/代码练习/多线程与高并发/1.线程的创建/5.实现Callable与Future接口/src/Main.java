import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 方式一：
        ExecutorService es = Executors.newCachedThreadPool();
        // 存放未来将返回的结果
        Future<String> f = es.submit(new MyCall()); // thread start!!!
        // 获取返回值
        String s = f.get();
        System.out.println(s); // success
        es.shutdown();

        // 方式二：
        FutureTask<String> task = new FutureTask<>(new MyCall());
        Thread t = new Thread(task);
        t.start(); // thread start!!!
        System.out.println(task.get()); // success
    }
}
