package factories;

import products.AddOperation;
import products.Operation;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 生产加法产品的工厂
 */
public class AddOperationFactory implements OperationFactory {
    @Override
    public Operation createOperation(double numberA, double numberB) {
        System.out.println("创建加法产品……");
        return new AddOperation(numberA, numberB);
    }
}
