public class Cat extends Animal {
    // 抽象类的子类，需要满足以下至少其中一个条件：
    // 1.也是抽象类。
    // 2.重写父类所有的抽象方法。

    @Override
    public void eat() {
        System.out.println("猫吃鱼");
    }
}
