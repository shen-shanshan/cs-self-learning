import java.util.Timer;

public class Test {
    public static void main(String[] args) {
        // 创建定时器对象
        Timer t = new Timer();
        // 延时 delay 毫秒后执行 task 中 run() 的内容
        // t.schedule(new MyTask(t), 3000);

        // 每间隔 period 毫秒执行一次，循环执行
        t.schedule(new MyTask(t), 3000, 3000);
    }
}
