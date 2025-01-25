import charge.ChargeImpl;
import exceptions.InvalidParamException;
import strategies.DiscountStrategy;
import strategies.NormalStrategy;
import strategies.ReturnStrategy;

public class Main {
    public static void main(String[] args) {
        test1(1000);
    }

    public static void test1(double money) {
        // 1.普通策略
        System.out.println("NormalStrategy: " + new ChargeImpl(new NormalStrategy()).ChargeFees(money));

        // 2.打折策略
        try {
            System.out.println("DiscountStrategy: " + new ChargeImpl(new DiscountStrategy((float) 0.75)).ChargeFees(money));
        } catch (InvalidParamException e) {
            throw new RuntimeException(e);
        }

        // 3.满减策略
        try {
            System.out.println("ReturnStrategy: " + new ChargeImpl(new ReturnStrategy(300, 50)).ChargeFees(money));
        } catch (InvalidParamException e) {
            throw new RuntimeException(e);
        }
    }
}