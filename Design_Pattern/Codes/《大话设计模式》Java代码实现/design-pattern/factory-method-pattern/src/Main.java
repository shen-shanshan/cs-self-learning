import factories.AddOperationFactory;
import factories.MultiOperationFactory;
import factories.OperationFactory;

public class Main {
    public static void main(String[] args) {
        // 加法运算
        OperationFactory factory = new AddOperationFactory();
        System.out.println(factory.createOperation(1, 34).getResult());

        // 乘法运算
        factory = new MultiOperationFactory();
        System.out.println(factory.createOperation(1, 34).getResult());
    }
}