package factories;

import products.MultiOperation;
import products.Operation;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 生产乘法产品的工厂
 */
public class MultiOperationFactory implements OperationFactory {
    @Override
    public Operation createOperation(double numberA, double numberB) {
        System.out.println("创建乘法产品……");
        return new MultiOperation(numberA, numberB);
    }
}
