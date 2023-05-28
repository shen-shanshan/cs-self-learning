public class Test {
    public static void main(String[] args) {
        // 创建一个 Runnable 对象，可以被多个 Thread 作为参数传入
        MyRunnable my = new MyRunnable();
        Thread t1 = new Thread(my);
        t1.setName("yyds");
        t1.start();
    }
}
