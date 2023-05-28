public class MyRunnable implements Runnable {
    public void run() {
        // 要获取线程名，只能间接调用 getName() 方法，因为该类没有继承 Thread 类
        System.out.println(Thread.currentThread().getName());
    }
}
