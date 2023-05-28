//public class Main {
//    private static boolean ready = false;
//    private static int number;
//
//    private static class MyThread extends Thread {
//        @Override
//        public void run() {
//            while (!ready) {
//                Thread.yield();
//            }
//            System.out.println(number);
//        }
//    }
//
//    public static void main(String[] args) {
//        Thread t = new MyThread();
//        t.start();
//        // 下面这两条语句没有依赖关系，可能会发生乱序执行
//        number = 42;
//        ready = true;
//    }
//}

import java.io.IOException;

public class Main {
    private int num = 8;

    public Main() {
        new Thread(() -> {
            System.out.println(num);
        }).start();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}