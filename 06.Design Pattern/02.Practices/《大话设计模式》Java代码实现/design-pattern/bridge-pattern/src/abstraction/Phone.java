package abstraction;

import implementor.Implementor;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-19
 * @Description: 抽象基类
 */
public abstract class Phone {
    // 组合具体的实现
    protected Implementor impl;

    public Phone(Implementor impl) {
        this.impl = impl;
    }

    public abstract void run();
}
