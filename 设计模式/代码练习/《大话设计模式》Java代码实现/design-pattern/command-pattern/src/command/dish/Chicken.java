package command.dish;

import command.Chef;
import command.Dish;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 具体菜品（ConcreteCommand）-烤鸡腿
 */
public class Chicken implements Dish {
    private final Chef chef;

    public Chicken(Chef chef) {
        this.chef = chef;
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public void execute() {
        chef.makeChicken();
    }
}
