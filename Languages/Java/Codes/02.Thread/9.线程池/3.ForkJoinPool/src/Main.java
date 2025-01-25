import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Main {
    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool();
        // AddTask task = new AddTask(0, 10000);
        // fjp.execute();
    }
}

//class AddTask implements {
//    int start;
//    int end;
//
//    public AddTask(int start, int end) {
//        this.start = start;
//        this.end = end;
//    }
//
//    public void fork() {
//
//    }
//}
