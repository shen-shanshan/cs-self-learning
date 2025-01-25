import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    // 线程池里的每一个线程代码结束后，并不会死亡，而是再次回到线程池中成为空闲状态，等待下一个对象来使用。
    public static void main(String[] args) {
        // 通过 Executors 工厂类来创建线程池对象，池里有 3 个线程
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            // 将任务提交到线程池
            // 方式一：传入一个实现了 Runnable 接口的实现类对象
            // pool.submit(new MyRunnable());
            // 方式二：匿名内部类
            pool.submit(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "启动");
                }
            });
        }
        // 结束线程池
        pool.shutdown();
    }
}
