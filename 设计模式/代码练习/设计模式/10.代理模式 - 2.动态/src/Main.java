import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        // 反射生成动态代理
        Movable m = (Movable) Proxy.newProxyInstance(Car.class.getClassLoader()
                , new Class[]{Movable.class}, new LogHandler(new Car()));
        // 执行方法，实际调用的是LogHandler中的invoke()
        m.move();
        // method move start...
        // 开车！
        // method move end!
    }
}


