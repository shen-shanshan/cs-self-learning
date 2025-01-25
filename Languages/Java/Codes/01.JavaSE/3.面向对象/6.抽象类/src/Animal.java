abstract class Animal {
    // 有抽象方法的类，一定是抽象类
    // 抽象类不一定有抽象方法

    // 抽象类有构造方法，用于子类访问父类数据的初始化
    public Animal() {
    }

    // 抽象方法：只给出声明，没有方法体（具体实现）
    public abstract void eat();
}
