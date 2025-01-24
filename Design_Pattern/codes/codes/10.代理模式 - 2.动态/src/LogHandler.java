import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 日志代理，可以供任何实现了Movable接口的对象使用
public class LogHandler implements InvocationHandler {

    Movable m;

    public LogHandler(Movable m){
        this.m = m;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1.这里可以通过对method进行判断来实现对不同方法做不同的处理。
        System.out.println("method " + method.getName() + " start...");
        // 2.这里可以通过传入不同的对象（要实现Movable接口）实现对不同对象的代理
        //   这里传入的是谁的引用，就调用谁的方法
        //   在本例中，这里的method就是move方法
        Object o = method.invoke(m, args);
        System.out.println("method " + method.getName() + " end!");
        // 这里实际上返回的是一个空值
        return o;
    }
}
