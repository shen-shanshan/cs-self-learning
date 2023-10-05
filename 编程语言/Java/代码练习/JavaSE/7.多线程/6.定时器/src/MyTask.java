import java.util.Timer;
import java.util.TimerTask;

public class MyTask extends TimerTask {
    private Timer t;

    public MyTask(Timer t) {
        this.t = t;
    }

    public void run() {
        // 要执行的任务内容
        System.out.println("执行一次任务");
        // 结束任务
        // t.cancel();
    }
}
