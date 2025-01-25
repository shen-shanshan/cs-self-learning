package factories;

import products.Operation;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 抽象工厂
 */
public interface OperationFactory {
    /**
     * 工厂方法：创建抽象产品
     *
     * @return 抽象产品
     */
    Operation createOperation(double numberA, double numberB);
}
