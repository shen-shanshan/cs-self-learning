public class Test {
    public static void main(String[] args) {
        MyThread my1 = new MyThread();
        // 启动线程，并由 jvm 去调用该线程的 run() 方法
        my1.start();
        // my.start();
        // Exception in thread "main" java.lang.IllegalThreadStateException
        // 非法的线程状态异常
        MyThread my2 = new MyThread();
        my2.start();

        // 获取线程的优先级
        // 优先级高的线程使用 CPU 的几率更高
        int p1 = my1.getPriority();
        // 默认优先级为 5。
        System.out.println(p1);
        // my1.setPriority(15);
        // Exception in thread "main" java.lang.IllegalArgumentException
        // 优先级级别范围：1 ~ 10。
        my1.setPriority(10);
        System.out.println(my1.getPriority());

        System.out.println(my1.getName());
        System.out.println(my2.getName());
        // 用于在不是 thread 的子类中获取线程对象的名称 - main
        System.out.println(Thread.currentThread().getName());
    }
}
