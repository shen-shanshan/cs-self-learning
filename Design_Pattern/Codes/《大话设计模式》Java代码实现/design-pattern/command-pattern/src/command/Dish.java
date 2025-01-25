package command;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 菜品接口（Command）
 */
public interface Dish {
    int getPrice();

    void execute();
}
