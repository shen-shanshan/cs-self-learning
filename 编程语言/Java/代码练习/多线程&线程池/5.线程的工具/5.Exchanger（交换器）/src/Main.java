import java.util.concurrent.Exchanger;

public class Main {
    // 创建交换器
    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            String s = "t1";
            try {
                // exchange()是阻塞方法
                // 完成数据的交换后才继续往下执行，保证数据的一致性
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "的s是：" + s);
        }, "t1").start();

        new Thread(() -> {
            String s = "t2";
            try {
                // 阻塞，交换数据
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "的s是：" + s);
        }, "t2").start();

        // 结果：
        // t1的s是：t2
        // t2的s是：t1
    }
}
