package state;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 抽象状态类
 */
public abstract class State {
    public abstract void handle(DevWork work);
}
