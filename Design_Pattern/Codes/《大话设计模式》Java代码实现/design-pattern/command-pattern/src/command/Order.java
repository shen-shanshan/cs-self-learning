package command;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 订单类（Invoker）
 */
public class Order {
    // 记录已点的菜品
    private final List<Dish> dishes;

    public Order() {
        dishes = new ArrayList<>();
    }

    // 增加菜品
    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    // 取消菜品
    public void cancelDish(Dish dish) {
        dishes.remove(dish);
    }

    // 结账
    public int getPrice() {
        int sum = 0;
        for (Dish dish : dishes) {
            sum += dish.getPrice();
        }
        return sum;
    }

    // 提交订单，做菜
    public void submit() {
        for (Dish dish : dishes) {
            dish.execute();
        }
    }
}
