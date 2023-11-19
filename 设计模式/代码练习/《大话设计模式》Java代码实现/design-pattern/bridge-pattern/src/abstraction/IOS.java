package abstraction;

import implementor.Implementor;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-19
 * @Description: 苹果手机（抽象层次）
 */
public class IOS extends Phone {
    public IOS(Implementor impl) {
        super(impl);
    }

    @Override
    public void run() {
        System.out.println("在苹果系统上" + impl.run());
    }
}
