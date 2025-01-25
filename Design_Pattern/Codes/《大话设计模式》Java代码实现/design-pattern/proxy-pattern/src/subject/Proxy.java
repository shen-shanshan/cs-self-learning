package subject;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-29
 * @Description: 代理类
 */
public class Proxy implements Subject {
    private String name;

    private Subject subject;

    public Proxy(String name, Subject subject) {
        this.name = name;
        this.subject = subject;
    }

    @Override
    public void request() {
        System.out.println("我是" + name + "，我为别人代言！");
        subject.request();
    }
}
