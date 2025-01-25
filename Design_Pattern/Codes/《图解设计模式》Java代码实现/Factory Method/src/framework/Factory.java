package framework;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 工厂方法模式 - 抽象工厂
 */
public abstract class Factory {

    protected abstract Product createProduct(String owner, int number);

    protected abstract void registerProduct(Product p);

    public final Product create(String owner, int number) {
        Product p = createProduct(owner, number);
        registerProduct(p);
        return p;
    }

}
