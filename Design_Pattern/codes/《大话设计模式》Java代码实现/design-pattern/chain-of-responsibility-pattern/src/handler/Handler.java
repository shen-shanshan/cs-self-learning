package handler;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 抽象处理类
 */
public abstract class Handler {
    protected Handler next;

    public Handler() {
    }

    public Handler(Handler next) {
        this.next = next;
    }

    public abstract boolean handle(String request);
}
