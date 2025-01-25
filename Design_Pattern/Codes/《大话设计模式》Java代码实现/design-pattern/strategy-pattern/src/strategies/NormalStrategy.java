package strategies;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-22
 * @Description: 普通策略（无优惠）
 */
public class NormalStrategy extends CostStrategy {
    @Override
    public double calculateMoney(double money) {
        // 不打折，原价支付
        return money;
    }
}
