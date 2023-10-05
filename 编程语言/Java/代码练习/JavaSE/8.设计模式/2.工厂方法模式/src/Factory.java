public interface Factory {
    // 抽象工厂类负责定义创建对象的接口
    // 具体对象的创建由实现该接口的具体工厂类来实现
    public abstract Animal createAnimal();
}
