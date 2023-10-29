package subject;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-29
 * @Description: 被代理类
 */
public class RealSubject implements Subject {
    private String name;

    public RealSubject(String name) {
        this.name = name;
    }

    @Override
    public void request() {
        System.out.println(name + "被代理了！");
    }
}
