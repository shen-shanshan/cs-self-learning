package strategies;

import exceptions.InvalidParamException;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-22
 * @Description: 打折策略
 */
public class DiscountStrategy extends CostStrategy {
    // 打折系数：0-1
    float ratio;

    public DiscountStrategy(float ratio) throws InvalidParamException {
        if (ratio <= 0 || ratio >= 1) {
            throw new InvalidParamException("ratio");
        }
        this.ratio = ratio;
    }

    @Override
    public double calculateMoney(double money) {
        return money * ratio;
    }
}
