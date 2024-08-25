import java.util.concurrent.Callable;

public class MyCall implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("thread start!!!");
        return "success";
    }
}
