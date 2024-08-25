// 日志代理，该类只负责记录move()方法的运行日志
public class CarLogProxy implements Moveable {
    // 代理中包含这个接口的引用
    Moveable car;

    public CarLogProxy(Moveable car) {
        this.car = car;
    }

    @Override
    public void move() {
        System.out.println("开始");
        // 执行方法
        car.move();
        System.out.println("结束");
    }
}
