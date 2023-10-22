package charge;

import strategies.CostStrategy;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-22
 * @Description: 具体的结账类
 */
public class ChargeImpl implements Charge {
    // 关联一个策略类
    CostStrategy strategy;

    public ChargeImpl(CostStrategy strategy) {
        this.strategy = strategy;
    }

    public ChargeImpl(String strategyName) {
        // TODO: 利用反射初始化策略类，使客户端与具体的策略类解耦
        try {
            this.strategy = (CostStrategy)Class.forName(strategyName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public double ChargeFees(double money) {
        return strategy.calculateMoney(money);
    }
}
