public class Test {
    public static void main(String[] args) {
        // 'Animal' 为 abstract；无法实例化
        // Animal a = new Animal();

        // 只能通过具体的子类来实例化
        Animal a = new Cat();
        a.eat();
    }
}
