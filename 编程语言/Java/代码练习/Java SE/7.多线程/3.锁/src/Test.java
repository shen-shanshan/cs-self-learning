public class Test {
    public static void main(String[] args) {
        SellTickets st = new SellTickets();
        Thread t1 = new Thread(st, "窗口1");
        Thread t2 = new Thread(st, "窗口2");
        Thread t3 = new Thread(st, "窗口3");
        t3.setPriority(9);
        t1.start();
        t2.start();
        t3.start();
    }
}
