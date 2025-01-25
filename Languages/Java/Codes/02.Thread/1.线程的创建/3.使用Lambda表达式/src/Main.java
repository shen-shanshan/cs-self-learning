public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("thread start!!!");
        }).start();
        // thread start!!!
    }
}
