import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            // 输出范围：(0,7]，即不包含 7
            System.out.println(r.nextInt(7));
        }
    }
}
