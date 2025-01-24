import abstraction.AndroidOS;
import abstraction.HarmonyOS;
import abstraction.IOS;
import abstraction.Phone;
import implementor.Book;
import implementor.Camera;

public class Main {
    public static void main(String[] args) {
        // 桥接模式：分离了抽象与具体两个层次，使我们可以自由地组合和扩展抽象与具体层次的类型
        Phone phoneSoftware1 = new AndroidOS(new Camera());
        phoneSoftware1.run();

        Phone phoneSoftware2 = new HarmonyOS(new Book());
        phoneSoftware2.run();

        Phone phoneSoftware3 = new IOS(new Camera());
        phoneSoftware3.run();
    }
}