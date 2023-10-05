public class Test {
    public static void main(String[] args) {
        MyRunnable my = new MyRunnable();
        Thread t1 = new Thread(my, "线程1");
        Thread t2 = new Thread(my, "线程2");

        // 获取线程组
        ThreadGroup tg1 = t1.getThreadGroup();
        // 获取线程组名称
        String name = tg1.getName();
        // 默认属于 main 线程组
        System.out.println(name);

        // 新建线程组
        ThreadGroup tg2 = new ThreadGroup("新建线程组");
        Thread t3 = new Thread(tg2, my, "线程3");
        Thread t4 = new Thread(tg2, my, "线程4");
        System.out.println(tg2.getName());
        // 将该线程组的线程都设置为守护线程
        tg2.setDaemon(true);

        // 启动线程
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // 中断线程
        t1.interrupt();
        t2.interrupt();
    }
}
