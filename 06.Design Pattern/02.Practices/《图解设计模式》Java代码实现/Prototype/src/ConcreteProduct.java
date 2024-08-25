/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-13
 * @Description: 原型模式 - 具体原型的父类
 */
public class ConcreteProduct implements Product {

    @Override
    public void use(String s) {
    }

    @Override
    public Product createClone() {
        Product p = null;
        try {
            p = (Product) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return p;
    }

}
