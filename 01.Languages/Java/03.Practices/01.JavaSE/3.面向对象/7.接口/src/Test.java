public class Test {
    public static void main(String[] args) {
        // 接口不能直接实例化，要由多态的方式，由具体的子类来实例化
        AnimalTrain at = new Cat();
        at.jump();
    }
}


