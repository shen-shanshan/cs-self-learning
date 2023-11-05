package products;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 加法产品类
 */
public class AddOperation extends Operation {
    public AddOperation(double numberA, double numberB) {
        super(numberA, numberB);
    }

    @Override
    public double getResult() {
        System.out.println("numberA + numberB = " + (numberA + numberB));
        return numberA + numberB;
    }
}
