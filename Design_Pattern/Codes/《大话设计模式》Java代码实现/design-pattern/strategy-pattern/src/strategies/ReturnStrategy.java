package strategies;

import exceptions.InvalidParamException;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-22
 * @Description: 满减策略
 */
public class ReturnStrategy extends CostStrategy {
    // 满减条件
    double threshold;

    // 返利
    double returnMoney;

    public ReturnStrategy(double threshold, double returnMoney) throws InvalidParamException {
        if (returnMoney <= 0 || returnMoney >= threshold) {
            throw new InvalidParamException("threshold、returnMoney");
        }
        this.threshold = threshold;
        this.returnMoney = returnMoney;
    }

    @Override
    public double calculateMoney(double money) {
        return money - (int) (money / (int) threshold) * returnMoney;
    }
}
