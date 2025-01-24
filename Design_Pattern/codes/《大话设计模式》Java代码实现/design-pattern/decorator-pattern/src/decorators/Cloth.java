package decorators;

import components.Component;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-29
 * @Description: 具体装饰器-衣服
 */
public class Cloth extends Decorator {
    public Cloth(Component component) {
        super(component);
    }

    @Override
    public void show() {
        super.show();
        System.out.println("穿衣服");
    }
}
