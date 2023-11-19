package abstraction;

import implementor.Implementor;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-19
 * @Description: 安卓手机（抽象层次）
 */
public class AndroidOS extends Phone {
    public AndroidOS(Implementor impl) {
        super(impl);
    }

    @Override
    public void run() {
        System.out.println("在安卓系统上" + impl.run());
    }
}
