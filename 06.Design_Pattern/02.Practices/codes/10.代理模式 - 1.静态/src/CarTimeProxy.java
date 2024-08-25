// 时间代理，该类只负责记录move()方法的运行时间
public class CarTimeProxy implements Moveable {
    // 代理中包含这个接口的引用
    Moveable car;

    public CarTimeProxy(Moveable car) {
        this.car = car;
    }

    @Override
    public void move() {
        // 记录起始时间
        long start = System.currentTimeMillis();
        // 执行方法
        car.move();
        // 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start) / 1000 + "s");
    }
}
