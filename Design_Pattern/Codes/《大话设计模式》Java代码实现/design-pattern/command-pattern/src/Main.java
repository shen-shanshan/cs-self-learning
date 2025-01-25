import command.Chef;
import command.Order;
import command.dish.Chicken;
import command.dish.Fish;

public class Main {
    public static void main(String[] args) {
        Chef chef = new Chef();

        // 点菜
        Order order = new Order();
        order.addDish(new Chicken(chef));
        order.addDish(new Chicken(chef));
        order.addDish(new Fish(chef));
        order.submit();

        // 结账
        System.out.println("共消费：" + order.getPrice());
    }
}