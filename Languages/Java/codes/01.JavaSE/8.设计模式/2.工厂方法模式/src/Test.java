public class Test {
    public static void main(String[] args) {
        Factory f = new DogFactory();
        Animal a = f.createAnimal();// 造的是狗
        a.eat();
        // 有新的对象增加时，只需要增加一个具体的类及其工厂类
        // 优点：后期易于维护，增强了扩展性
        Factory f2 = new CatFactory();
        Animal a2 = f2.createAnimal();// 造的是猫
        a2.eat();
    }
}
