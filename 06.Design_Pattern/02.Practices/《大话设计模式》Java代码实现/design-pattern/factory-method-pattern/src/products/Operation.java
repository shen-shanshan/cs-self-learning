package products;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 抽象产品
 */
public abstract class Operation {
    public double numberA;
    public double numberB;

    public Operation(double numberA, double numberB) {
        this.numberA = numberA;
        this.numberB = numberB;
    }

    public abstract double getResult();
}
