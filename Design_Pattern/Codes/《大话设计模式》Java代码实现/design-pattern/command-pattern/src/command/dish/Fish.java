package command.dish;

import command.Chef;
import command.Dish;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 具体菜品（ConcreteCommand）-烤鱼
 */
public class Fish implements Dish {
    private final Chef chef;

    public Fish(Chef chef) {
        this.chef = chef;
    }

    @Override
    public int getPrice() {
        return 30;
    }

    @Override
    public void execute() {
        chef.makeFish();
    }
}
